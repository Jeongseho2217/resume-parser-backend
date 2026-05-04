package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AnalysisResult;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, String> {
}