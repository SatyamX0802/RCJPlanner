package com.planner.RCJPlanner.repository;

import com.planner.RCJPlanner.entities.UserJob;
import com.planner.RCJPlanner.entities.AppUser;
import com.planner.RCJPlanner.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJobRepository extends JpaRepository<UserJob, Long> {
    List<UserJob> findByUser(AppUser user);
    List<UserJob> findByJob(Job job);
    List<UserJob> findByJobIn(List<Job> jobs);
    Optional<UserJob> findByUserAndJob(AppUser user, Job job);
}
