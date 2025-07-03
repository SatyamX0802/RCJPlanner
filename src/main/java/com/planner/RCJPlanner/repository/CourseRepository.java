package com.planner.RCJPlanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planner.RCJPlanner.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {}