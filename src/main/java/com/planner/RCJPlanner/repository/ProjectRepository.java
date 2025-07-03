package com.planner.RCJPlanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planner.RCJPlanner.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {}