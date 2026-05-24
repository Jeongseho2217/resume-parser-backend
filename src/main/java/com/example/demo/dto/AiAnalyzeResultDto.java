// 분석 결과를 저장할 DTO입니다.

package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data 
public class AiAnalyzeResultDto {
    private List<String> summary; // 3줄 요약을 저장할 리스트
    private List<String> core_competencies; // 핵심 역량을 저장할 리스트
    private List<String> tech_stacks; // 직무 기술을 저장할 리스트
    private int Matching_score; // 직무 기술을 저장할 변수
    private String school;
    private String experience;
}