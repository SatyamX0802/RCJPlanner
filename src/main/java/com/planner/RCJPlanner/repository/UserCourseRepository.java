package com.planner.RCJPlanner.repository;

import com.planner.RCJPlanner.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {

    List<UserCourse> findByUser(AppUser user);

    List<UserCourse> findByUserAndStatus(AppUser user, String status); 
    
    Optional<UserCourse> findByUserAndCourseId(AppUser user, Long courseId);

}
