package com.example.demo.service;

import com.example.demo.dto.AiAnalyzeResultDto;
import com.example.demo.dto.OpenAiRequest;
import com.example.demo.dto.OpenAiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiAnalyzeResultDto analyzeWithAI(String resumeText, String jobRequirement) {
        
        //1. 프롬프트 하나에 '채용 공고', '이력서', 그리고 '매칭 점수'가 포함된 JSON 형식 반환
        String prompt = """
        너는 10년 차 IT 전문 채용 담당자야.
        아래 제공된 [채용 공고]와 지원자의 [자기소개서(이력서)]를 비교 분석하여,
        요약, 기술 스택, 핵심 역량을 추출하고 100점 만점의 매칭 점수를 계산해 줘.
        요약 부분은 한 개당 20자이내로 해줘. 기술 스택과 핵심 역량은 반드시 앞에 #을 붙여줘.
        다른 부연 설명이나 인사말은 절대 하지 말고 반드시 아래 주어진 JSON 형식으로만 응답해.

        [매칭도 채점 기준]
        1. 기술 스택 일치도 (40점): 공고에서 요구하는 기술을 지원자가 보유하고 있는가?
        2. 직무 경험 적합도 (40점): 공고의 업무 내용과 자소서의 프로젝트 경험이 유사한가?
        3. 소프트 스킬 및 핵심 역량 (20점): 공고가 원하는 인재상(협업, 문제해결 등)에 부합하는가?

        [응답 JSON 형식]
        {
          "summary": ["요약1", "요약2", "요약3"],
          "school": "최종 학력을 추출해서 적어줘 (예: 한국대학교 컴퓨터공학과). 없으면 빈 문자열",
          "experience": "총 경력 기간을 추출해서 적어줘 (예: 3년, 5년). 없으면 빈 문자열",
          "core_competencies": ["#역량1", "#역량2"],
          "tech_stacks": ["#기술1", "#기술2", "#기술3"],
          "matching_score": 0
        }

        [지시사항] 
        matching_score의 값은 위의 예시 숫자(0)를 그대로 복사하지 마세요.
        experience와 school 또한 예시(3년, 5년과 한국대학교 컴퓨터공학과) 그대로 복사하지 마세요.
        반드시 네가 채점 기준에 따라 직접 계산한 0에서 100 사이의 실제 점수(정수)를 입력해야 합니다. 직무가 다르면 낮은 점수를 부여하세요.

        [채용 공고 자격/우대사항]
        %s

        [지원자 자기소개서]
        %s
        """.formatted(jobRequirement, resumeText); // %s 자리에 공고와 자소서 끼우기

        OpenAiRequest request = new OpenAiRequest(
                model,
                List.of(new OpenAiRequest.Message("user", prompt))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);
        
        OpenAiResponse response = restTemplate.postForObject(apiUrl, entity, OpenAiResponse.class);

        // AI가 뱉어낸 순수 텍스트 결과물
        String aiJsonString = response.getChoices().get(0).getMessage().getContent();
        
        // 2. AI가 뭐라고 대답했는지 콘솔에서 직접 확인하기 위해 로그 찍기
        System.out.println("[AI Origin Response Check]: \n" + aiJsonString);
        
        try {
            return objectMapper.readValue(aiJsonString, AiAnalyzeResultDto.class);
        } catch (Exception e) {
            // 파싱 에러가 나면 콘솔에 찍힌 [AI 순수 응답 원본]을 보고 문제를 찾을 수 있습니다.
            throw new RuntimeException("AI 응답을 파싱하는데 실패했습니다. AI 응답을 확인하세요.", e);
        }
    }
}