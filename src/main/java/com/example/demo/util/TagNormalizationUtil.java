package com.example.demo.util;

import java.util.HashMap;
import java.util.Map;

public class TagNormalizationUtil {

    private static final Map<String, String> TAG_DICTIONARY = new HashMap<>();

    static {
        TAG_DICTIONARY.put("자바", "Java");
        TAG_DICTIONARY.put("java", "Java");
        TAG_DICTIONARY.put("스프링", "Spring");
        TAG_DICTIONARY.put("스프링부트", "Spring Boot");
        TAG_DICTIONARY.put("springboot", "Spring Boot");
        TAG_DICTIONARY.put("aws", "AWS");
        TAG_DICTIONARY.put("리액트", "React");
        TAG_DICTIONARY.put("react", "React");
        TAG_DICTIONARY.put("mysql", "MySQL");
    }

    public static String normalizeTag(String rawTag) {
        if (rawTag == null || rawTag.trim().isEmpty()) {
            return "";
        }
        String searchKey = rawTag.trim().toLowerCase().replace(" ", "");
        return TAG_DICTIONARY.getOrDefault(searchKey, rawTag.trim());
    }
}