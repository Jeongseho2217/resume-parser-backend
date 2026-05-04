package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, String> {
}