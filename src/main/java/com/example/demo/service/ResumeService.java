package com.example.demo.service;

import com.example.demo.dto.ResumeAnalyzeRequest;
import com.example.demo.dto.ResumeSubmitRequest;
import com.example.demo.dto.AiAnalyzeResultDto;
import com.example.demo.entity.Candidate;
import com.example.demo.entity.JobPosting;
import com.example.demo.entity.MemberType;
import com.example.demo.entity.Resume;
import com.example.demo.entity.ResumeStatus;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.JobPostingRepository;
import com.example.demo.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final CandidateRepository candidateRepository;
    private final ResumeRepository resumeRepository;
    private final JobPostingRepository jobPostingRepository;
    private final OpenAiService openAiService;

    // 동기 처리: 호출 즉시 DB에 PENDING 상태로 이력서를 저장하고 ID를 반환 (프론트 통신용)
    // ResumeAnalyzeRequest (또는 SubmitRequest)에서 데이터를 분할해서 받아 DB에 저장함
    @Transactional
    public Long saveResumeAsPending(ResumeSubmitRequest request) { // ★ 주의: 프론트에서 3분할로 보내는 DTO 객체를 받도록 수정됨
        JobPosting jobPosting = jobPostingRepository.findById(request.getJobPostingId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공고 ID입니다: " + request.getJobPostingId()));

        // 지원자 등록 
        Candidate candidate = new Candidate();
        candidate.setName(request.getCandidateName());
        candidate.setEmail(request.getEmail());
        candidate.setMemberType(MemberType.MEMBER);
        candidate.setJobPosting(jobPosting);
        candidate.setPassword(request.getPassword()); // 비밀번호 저장

        Candidate savedCandidate = candidateRepository.save(candidate);

        // 이력서 3분할 저장 및 PENDING 설정
        Resume resume = new Resume();
        resume.setCandidate(savedCandidate);
        resume.setJobPosting(jobPosting);
        
        // ★ 교수님 피드백 반영: 3분할 데이터 저장
        resume.setMotivation(request.getMotivation());
        resume.setTechStack(request.getTechStack());
        resume.setProjectExperience(request.getProjectExperience());
        
        resume.setStatus(ResumeStatus.PENDING); // 상태를 일단 PENDING으로
        
        Resume savedResume = resumeRepository.save(resume);
        return savedResume.getId();
    }

    // 비동기 처리: 별도의 스레드에서 LM Studio를 호출하고 결과를 DB에 업데이트
    @Async
    @Transactional
    public void processAiAnalysisAsync(Long resumeId) { // 이제 텍스트를 파라미터로 안 받고 DB에서 직접 조합해서 씀
        try {
            System.out.println("[AI 분석 시작] 이력서 ID: " + resumeId + " 백그라운드 분석을 시작합니다.");
            
            // 1. DB에서 방금 저장된 이력서 정보 찾아오기
            Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이력서 ID입니다: " + resumeId));
            
            // 2. 공고의 자격 요건 가져오기
            String jobRequirement = resume.getJobPosting().getRequirement(); 
            
            // ==========================================
            // AI 프롬프트 최적화: 환각 방지를 위한 분석 대상 텍스트 조합
            // 지원 동기(motivation)는 감성적 영역이므로 AI 핵심 기술 분석에서는 제외하거나 비중을 낮춤
            // ==========================================
            String textToAnalyze = 
                " [보유 기술 스택] " + resume.getTechStack() + 
                " [핵심 프로젝트 경험] " + resume.getProjectExperience();

            // 3. 조합된 '핵심 텍스트'와 '채용 공고'를 AI에게 던짐
            AiAnalyzeResultDto aiResult = openAiService.analyzeWithAI(textToAnalyze, jobRequirement);

            // 4. 추출된 데이터 맵핑
            if (aiResult.getSchool() != null) {
                resume.setSchool(aiResult.getSchool().length() > 100 ? aiResult.getSchool().substring(0, 100) : aiResult.getSchool());
            }
            if (aiResult.getExperience() != null) {
                resume.setExperience(aiResult.getExperience().length() > 100 ? aiResult.getExperience().substring(0, 100) : aiResult.getExperience());
            }
            
            com.example.demo.entity.AnalysisResult analysisResult = new com.example.demo.entity.AnalysisResult();
            analysisResult.setResume(resume); 
            analysisResult.setSummary(aiResult.getSummary()); 
            analysisResult.setTechnicalSkills(aiResult.getTech_stacks()); 
            analysisResult.setCoreCompetencies(aiResult.getCore_competencies()); 
            analysisResult.setMatchingScore(aiResult.getMatching_score()); 
            
            // 5. 이력서 엔티티에 조립 및 상태 업데이트
            resume.setAnalysisResult(analysisResult);
            resume.setStatus(ResumeStatus.DONE);
            
            // 6. 저장 완료
            resumeRepository.save(resume);
            
            System.out.println("[AI 분석 완료] 이력서 ID: " + resumeId + " 매칭 점수: " + aiResult.getMatching_score() + "점");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[AI 분석 실패] 이력서 ID: " + resumeId + " 처리 중 오류: " + e.getMessage());
            resumeRepository.findById(resumeId).ifPresent(resume -> resume.setStatus(ResumeStatus.FAILED));
        }
    }
}