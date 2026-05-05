package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class OpenAiResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private OpenAiRequest.Message message; // 아까 만든 Message 클래스 재사용
    }
}