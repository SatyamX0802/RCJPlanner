package com.planner.RCJPlanner.config;

import com.planner.RCJPlanner.entities.Course;
import com.planner.RCJPlanner.repository.CourseRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder {

    @Autowired
    private CourseRepository courseRepository;

    @PostConstruct
    public void seedCourses() {
        if (courseRepository.count() == 0) {
            List<Course> courses = List.of(
                new Course("JavaScript for Beginners", "Understand the basics of JavaScript programming.", "Udacity", "https://www.udacity.com/course/javascript-basics--ud804"),
                new Course("REST APIs with Spring Boot", "Build robust RESTful APIs using Spring Boot.", "Coursera", "https://www.coursera.org/learn/rest-apis-spring-boot"),
                new Course("Tailwind CSS Complete Guide", "Design sleek UIs with Tailwind CSS.", "Scrimba", "https://scrimba.com/learn/tailwind"),
                new Course("Docker for Developers", "Containerize applications with Docker.", "Udemy", "https://www.udemy.com/course/docker-mastery/"),
                new Course("AWS Fundamentals", "Learn core AWS services and architecture.", "edX", "https://www.edx.org/course/aws-fundamentals"),
                new Course("Intro to Machine Learning", "Learn basics of ML using Scikit-Learn.", "Kaggle Learn", "https://www.kaggle.com/learn/intro-to-machine-learning")
                // Add more courses here
            );
            courseRepository.saveAll(courses);
            System.out.println("Course data seeded.");
        }
    }
}
