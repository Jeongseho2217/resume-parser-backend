package com.example.demo.service;

import com.example.demo.dto.ResumeAnalyzeRequest;
import com.example.demo.dto.ResumeAnalyzeResponse;
import com.example.demo.dto.ResumeSubmitRequest;
import com.example.demo.entity.Candidate;
import com.example.demo.entity.JobPosting;
import com.example.demo.entity.MemberType;
import com.example.demo.entity.Resume;
import com.example.demo.entity.ResumeStatus;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.JobPostingRepository;
import com.example.demo.repository.ResumeRepository;
import com.example.demo.dto.AiAnalyzeResultDto;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final CandidateRepository candidateRepository;
    private final ResumeRepository resumeRepository;
    private final JobPostingRepository jobPostingRepository;
    private final OpenAiService openAiService;

    @Transactional
    public void saveResumeAndCandidate(ResumeSubmitRequest dto) {
        
        // DB에서 실제 존재하는 공고 엔티티를 조회
        JobPosting jobPosting = jobPostingRepository.findById(dto.getJobPostingId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공고 ID입니다: " + dto.getJobPostingId()));

        // 지원자 정보 세팅
        Candidate candidate = new Candidate();
        candidate.setName(dto.getCandidateName());
        candidate.setEmail(dto.getEmail());
        candidate.setPassword(dto.getPassword());
        candidate.setMemberType(MemberType.MEMBER); 
        candidate.setJobPosting(jobPosting);

        Candidate savedCandidate = candidateRepository.save(candidate);

        // 이력서 정보 세팅
        Resume resume = new Resume();
        resume.setCandidate(savedCandidate); 
        resume.setResumeText(dto.getResumeText());
        resume.setStatus(ResumeStatus.PENDING);

        resume.setJobPosting(jobPosting);

        resumeRepository.save(resume);
    }

public AiAnalyzeResultDto analyzeResume(ResumeAnalyzeRequest request) {

        String testResumeText = """
                안녕하세요. 백엔드 및 게임 서버 개발자를 꿈꾸는 지원자입니다.
                주요 기술 스택으로는 Java, Spring Boot, PostgreSQL, Docker를 활용할 수 있으며 Luau 스크립팅과 물리 엔진 제어에 능숙합니다.
                
                [진행 프로젝트 1: AI 기반 이력서 분석 시스템]
                Spring Boot와 OpenAI API를 활용하여 이력서를 분석하고 면접 질문을 자동 생성하는 플랫폼을 개발했습니다. PostgreSQL의 pgvector 확장과 JSONB를 활용하여 AI가 분석한 벡터 데이터와 비정형 데이터를 효율적으로 저장하고 검색할 수 있도록 데이터베이스 스키마와 ERD를 직접 설계했습니다.
                
                [진행 프로젝트 2: 멀티 미니게임 로비 및 매칭 시스템]
                다수의 플레이어가 대기하고 매칭될 수 있는 로비 시스템을 구축했습니다. UI 프레임과 스크립트를 활용해 방 선택, 인원수 동기화, 화면 전환 로직을 구현했으며, 모바일과 PC 환경을 모두 지원하는 커스텀 카메라 컨트롤러를 개발하여 사용자 경험(UX)을 개선했습니다.
                
                소프트웨어 공학의 애자일(Agile) 방법론과 COCOMO, Putnam 같은 비용 산정 모델을 학습하여 효율적인 프로젝트 개발 주기에 관심이 많습니다. 문제 해결을 좋아하며 끈기 있게 시스템 트러블슈팅을 해내는 것이 저의 가장 큰 무기입니다.
                """; //테스트용 하드코딩
        
        AiAnalyzeResultDto aiResult = openAiService.analyzeWithAI(testResumeText);
        
        // 콘솔에 출력
        System.out.println("AI의 분석 결과: " + aiResult);

        // 변환된 DTO 자체를 컨트롤러로 투척
        return aiResult;
    }
}