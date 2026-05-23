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

// ResumeService <-> ResumeController

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final CandidateRepository candidateRepository;
    private final ResumeRepository resumeRepository;
    private final JobPostingRepository jobPostingRepository;
    private final OpenAiService openAiService;

    @Transactional
    public void saveResumeAndCandidate(ResumeSubmitRequest dto) { // 이력서 제출 로직
        JobPosting jobPosting = jobPostingRepository.findById(dto.getJobPostingId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공고 ID입니다: " + dto.getJobPostingId()));

        Candidate candidate = new Candidate(); // 지원자 객체 생성 및 정의
        candidate.setName(dto.getCandidateName());
        candidate.setEmail(dto.getEmail());
        candidate.setPassword(dto.getPassword());
        candidate.setMemberType(MemberType.MEMBER);
        candidate.setJobPosting(jobPosting);

        Candidate savedCandidate = candidateRepository.save(candidate);

        Resume resume = new Resume(); // 이력서 객체 생성 및 정의
        resume.setCandidate(savedCandidate);
        resume.setResumeText(dto.getResumeText());
        resume.setStatus(ResumeStatus.PENDING);
        resume.setJobPosting(jobPosting);

        resumeRepository.save(resume);
    }

    //동기 처리: 호출 즉시 DB에 PENDING 상태로 이력서를 저장하고 ID를 반환.
    @Transactional
    public Long saveResumeAsPending(ResumeAnalyzeRequest request) {
        JobPosting jobPosting = jobPostingRepository.findById(request.job_id())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공고 ID입니다: " + request.job_id()));

        // 지원자 등록 (기존에 없는 경우 새로 생성한다고 가정)
        Candidate candidate = new Candidate();
        candidate.setName(request.name());
        candidate.setEmail(request.email());
        candidate.setMemberType(MemberType.MEMBER);
        candidate.setJobPosting(jobPosting);

        //DB의 NOT NULL 조건을 통과하기 위해 임시 비밀번호를 세팅.
        candidate.setPassword("temp_password_1234!");

        Candidate savedCandidate = candidateRepository.save(candidate);

        // 이력서 상태를 PENDING으로 우선 저장
        Resume resume = new Resume();
        resume.setCandidate(savedCandidate);
        resume.setResumeText(request.resume_text());
        resume.setStatus(ResumeStatus.PENDING);
        resume.setJobPosting(jobPosting);
        
        Resume savedResume = resumeRepository.save(resume);
        return savedResume.getId();
    }

    // 비동기 처리: 별도의 스레드에서 LM Studio를 호출하고 결과를 DB에 업데이트.
    // @Async가 작동하려면 이 메서드는 반드시 외부 컨트롤러에서 호출되어야 함.
    @Async
    @Transactional
    public void processAiAnalysisAsync(Long resumeId, String resumeText) {
        try {
            System.out.println("[AI 분석 시작] 이력서 ID: " + resumeId + " 백그라운드 분석을 시작합니다.");
            
            // 1. AI를 부르기 전에, DB에서 이력서와 '채용 공고' 정보를 먼저 찾아옵니다!
            Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이력서 ID입니다: " + resumeId));
            
            // 공고 테이블(JobPosting)에서 자격요건(requirement) 텍스트를 꺼내옵니다.
            String jobRequirement = resume.getJobPosting().getRequirement(); 
            
            // 2. AI에게 자소서와 채용 공고를 '같이' 던집니다.
            AiAnalyzeResultDto aiResult = openAiService.analyzeWithAI(resumeText, jobRequirement);
            
            // 3. AI 분석 결과를 실제 DB 엔티티(AnalysisResult)에 담기
            com.example.demo.entity.AnalysisResult analysisResult = new com.example.demo.entity.AnalysisResult();
            analysisResult.setResume(resume); 
            
            analysisResult.setSummary(aiResult.getSummary()); 
            analysisResult.setTechnicalSkills(aiResult.getTech_stacks()); 
            analysisResult.setCoreCompetencies(aiResult.getCore_competencies()); 
            
            // 4. 대망의 매칭 점수 세팅! (이제 진짜 AI가 계산한 점수가 들어갑니다)
            analysisResult.setMatchingScore(aiResult.getMatching_score()); 
            
            // 5. 이력서 엔티티에 결과물 조립하고 상태를 DONE으로 변경
            resume.setAnalysisResult(analysisResult);
            resume.setStatus(ResumeStatus.DONE);
            
            // 6. DB에 최종 저장
            resumeRepository.save(resume);
            
            System.out.println("[AI Analyze Done] Resume ID: " + resumeId + " (Matching Score: " + aiResult.getMatching_score() + ") Save"); // 콘솔창에 띄울 메시지
            
        } catch (Exception e) {
            System.err.println("[AI Analyze Failed] Resume ID: " + resumeId + " 처리 중 오류 발생: " + e.getMessage());
            resumeRepository.findById(resumeId).ifPresent(resume -> resume.setStatus(ResumeStatus.FAILED));
        }
    }
}