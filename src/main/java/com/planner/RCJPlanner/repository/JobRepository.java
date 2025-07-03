package com.planner.RCJPlanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planner.RCJPlanner.entities.AppUser;
import com.planner.RCJPlanner.entities.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByPostedBy(AppUser recruiter);
    List<Job> findByStatus(String status);
    
}