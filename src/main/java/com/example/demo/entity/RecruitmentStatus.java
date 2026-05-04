package com.example.demo.entity;

public enum RecruitmentStatus {
    REVIEWING("검토중"),
    ACCEPTED("합격"),
    REJECTED("불합격");

    private final String displayName;

    RecruitmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}