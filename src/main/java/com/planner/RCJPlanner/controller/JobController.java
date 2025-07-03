package com.planner.RCJPlanner.controller;

import com.planner.RCJPlanner.entities.*;
import com.planner.RCJPlanner.repository.*;
import com.planner.RCJPlanner.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired private JobRepository jobRepo;
    @Autowired private UserJobRepository userJobRepo;
    @Autowired private AppUserRepository userRepo;

    // ✅ Show Open Jobs & Applied Jobs
    @GetMapping
    public String showJobs(Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser user = userDetails.getUser();

        List<Job> openJobs = jobRepo.findByStatus("open");
        List<UserJob> myApplications = userJobRepo.findByUser(user);

        model.addAttribute("jobs", openJobs);
        model.addAttribute("myApplications", myApplications);

        return "jobs/my_jobs";
    }

    // User: View Own Applications
    @GetMapping("/my-jobs")
    public String myJobs(@AuthenticationPrincipal AppUserDetails userDetails, Model model) {
        AppUser user = userDetails.getUser();
        List<UserJob> applications = userJobRepo.findByUser(user);
        model.addAttribute("applications", applications);
        return "jobs/my_jobs";
    }

    
    @GetMapping("/all")
    public String viewAllJobs(Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser user = userDetails.getUser();

        List<Job> allJobs = jobRepo.findAll(); // includes open/closed
        List<UserJob> myApplications = userJobRepo.findByUser(user);

        // Extract job IDs applied by this user
        List<Long> appliedJobIds = myApplications.stream()
            .map(app -> app.getJob().getId())
            .toList();

        model.addAttribute("jobs", allJobs);
        model.addAttribute("appliedJobIds", appliedJobIds);

        return "jobs/all_jobs";
    }
    
 // Apply to a Job (only for users, not recruiters)
    @PostMapping("/apply/{jobId}")
    public String applyToJob(@PathVariable Long jobId,
                             @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser user = userDetails.getUser();

        Job job = jobRepo.findById(jobId).orElseThrow();
        Optional<UserJob> existingApp = userJobRepo.findByUserAndJob(user, job);

        if (existingApp.isEmpty()) {
            UserJob application = new UserJob(user, job, "applied", LocalDate.now());
            userJobRepo.save(application);
        }
        

        return "redirect:/jobs/all";

    }

    
}
