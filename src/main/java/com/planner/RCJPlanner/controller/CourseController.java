package com.planner.RCJPlanner.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.planner.RCJPlanner.entities.*;
import com.planner.RCJPlanner.repository.AppUserRepository;
import com.planner.RCJPlanner.repository.CourseRepository;
import com.planner.RCJPlanner.repository.UserCourseRepository;
import com.planner.RCJPlanner.security.AppUserDetails;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired private CourseRepository courseRepo;
    @Autowired private UserCourseRepository userCourseRepo;
    @Autowired private AppUserRepository userRepo;

    // Show My Courses and Available Courses
    @GetMapping
    public String showCourses(Model model, 
                              @AuthenticationPrincipal AppUserDetails userDetails,
                              @RequestParam(name = "filter", required = false) String filter) {

        AppUser user = userDetails.getUser();
        List<UserCourse> myCourses = userCourseRepo.findByUser(user);

        List<UserCourse> filteredCourses = myCourses;
        if (filter != null && !filter.isEmpty()) {
            filteredCourses = myCourses.stream()
                .filter(c -> c.getStatus().equalsIgnoreCase(filter))
                .toList();
        }
       

        List<Course> allCourses = courseRepo.findAll();
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("enrolledCourseIds", myCourses.stream()
                .map(uc -> uc.getCourse().getId())
                .toList());


        model.addAttribute("myCourses", filteredCourses);  // use filtered list
//        model.addAttribute("availableCourses", availableCourses);
        return "courses/list"; 
    }


    // Enroll in a Course
    @PostMapping("/enroll/{courseId}")
    public String enroll(@PathVariable Long courseId, @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        Course course = courseRepo.findById(courseId).orElseThrow();

        // Prevent duplicate enrollment
        Optional<UserCourse> existing = userCourseRepo.findByUserAndCourseId(user, courseId);
        if (existing.isEmpty()) {
            UserCourse uc = new UserCourse();
            uc.setUser(user);
            uc.setCourse(course);
            uc.setStatus("pending");
            userCourseRepo.save(uc);
        }

        return "redirect:/courses";
    }

    // Update Course Status (Mark Completed / Pending)
    @PostMapping("/update/{id}")
    public String updateCourseStatus(@PathVariable Long id,
                                     @RequestParam("status") String status,
                                     @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser user = userDetails.getUser();
        Optional<UserCourse> userCourseOpt = userCourseRepo.findById(id);
        if (userCourseOpt.isPresent()) {
            UserCourse userCourse = userCourseOpt.get();
            if (userCourse.getUser().getId().equals(user.getId())) {
                userCourse.setStatus(status);
                userCourseRepo.save(userCourse);
            }
        }
        return "redirect:/courses";
    }
    
 // Unenroll from a Course
    @PostMapping("/unenroll/{id}")
    public String unenroll(@PathVariable Long id, @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser user = userDetails.getUser();
        Optional<UserCourse> userCourseOpt = userCourseRepo.findById(id);
        
        if (userCourseOpt.isPresent()) {
            UserCourse userCourse = userCourseOpt.get();
            if (userCourse.getUser().getId().equals(user.getId())) {
                userCourseRepo.delete(userCourse);
            }
        }
        return "redirect:/courses";
    }


}
