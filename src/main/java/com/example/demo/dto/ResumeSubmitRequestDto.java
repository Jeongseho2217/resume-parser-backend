package com.example.demo.dto;

public class ResumeSubmitRequestDto {
    private String candidate_name;
    private String email;
    private String password;
    private Long job_posting_id;
    private String resume_text;

    public String getCandidate_name() { return candidate_name; }
    public void setCandidate_name(String candidate_name) { this.candidate_name = candidate_name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getJob_posting_id() { return job_posting_id; }
    public void setJob_posting_id(Long job_posting_id) { this.job_posting_id = job_posting_id; }

    public String getResume_text() { return resume_text; }
    public void setResume_text(String resume_text) { this.resume_text = resume_text; }
}