package com.planner.RCJPlanner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planner.RCJPlanner.entities.AppUser;
import com.planner.RCJPlanner.entities.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByUser(AppUser user);
}