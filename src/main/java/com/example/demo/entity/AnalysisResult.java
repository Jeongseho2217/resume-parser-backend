package com.example.demo.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "analysis_results")
@Getter
@Setter
@NoArgsConstructor
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id") // DB의 resume_id 컬럼과 연결
    private Resume resume;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> summary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "technical_skills", columnDefinition = "jsonb")
    private List<String> technicalSkills;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "core_competencies", columnDefinition = "jsonb")
    private List<String> coreCompetencies;

    @Column(name = "matching_score")
    private Integer matchingScore;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "model_name")
    private String modelName;
}