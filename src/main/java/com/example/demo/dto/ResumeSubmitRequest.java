package com.example.demo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResumeSubmitRequest {

    @NotBlank(message = "지원자 이름은 필수입니다.")
    private String candidateName;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotNull(message = "공고 ID는 필수입니다.")
    private Long jobPostingId;

    // ==========================================
    // 피드백 반영: 자소서 항목 분리 
    // ==========================================
    @NotBlank(message = "지원 동기 및 자기소개는 필수입니다.")
    private String motivation; // 스네이크 케이스 자동 매핑: motivation (AI 요약 및 성향 파악용)

    @NotBlank(message = "기술 스택은 필수입니다.")
    private String techStack; // 스네이크 케이스 자동 매핑: tech_stack (기술 역량 추출용)

    @NotBlank(message = "프로젝트 수행 경험은 필수입니다.")
    private String projectExperience; // 스네이크 케이스 자동 매핑: project_experience (학력/경력 및 핵심 성과 파싱용)
}