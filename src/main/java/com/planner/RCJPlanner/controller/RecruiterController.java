package com.planner.RCJPlanner.controller;

import com.planner.RCJPlanner.entities.AppUser;
import com.planner.RCJPlanner.entities.Job;
import com.planner.RCJPlanner.entities.UserJob;
import com.planner.RCJPlanner.repository.JobRepository;
import com.planner.RCJPlanner.repository.UserJobRepository;
import com.planner.RCJPlanner.security.AppUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/recruiter")
public class RecruiterController {

    @Autowired
    private JobRepository jobRepo;
    @Autowired 
    private UserJobRepository userJobRepo;

    @GetMapping("/dashboard")
    public String showRecruiterDashboard(Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser user = userDetails.getUser();
        List<Job> myJobs = jobRepo.findByPostedBy(user); //  get recruiter's jobs

        model.addAttribute("name", user.getFullName());
        model.addAttribute("myJobs", myJobs);
        return "recruiter/dashboard";
    }
    
 // Recruiter: View Own Posted Jobs
    @GetMapping("/my-posts")
    public String recruiterJobs(Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser recruiter = userDetails.getUser();
        List<Job> myPostedJobs = jobRepo.findByPostedBy(recruiter);

        model.addAttribute("myPostedJobs", myPostedJobs);
        return "recruiter/recruiter_jobs";
    }

    // Recruiter: Show Job Post Form
    @GetMapping("/post")
    public String showPostJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "recruiter/post_form";
    }

    // Recruiter: Submit Job Posting
    @PostMapping("/post")
    public String postJob(@ModelAttribute Job job,
                          @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser recruiter = userDetails.getUser();
        job.setPostedBy(recruiter);
        job.setStatus("open");
        job.setPostedDate(LocalDate.now());

        jobRepo.save(job);
        return "redirect:/recruiter/my-posts";
    }
    
 // Recruiter: View All Applications to Own Jobs
    @GetMapping("/applications")
    public String viewJobApplications(@AuthenticationPrincipal AppUserDetails userDetails, Model model) {
        AppUser recruiter = userDetails.getUser();

        // FIX: Use correct method name
        List<Job> postedJobs = jobRepo.findByPostedBy(recruiter);
        List<UserJob> allApplications = userJobRepo.findByJobIn(postedJobs);

        model.addAttribute("applications", allApplications);
        return "recruiter/recruiter-applications";
    }

    // Recruiter: Update Application Status
    @PostMapping("/update-status/{applicationId}")
    public String updateApplicationStatus(@PathVariable Long applicationId,
                                          @RequestParam String status,
                                          @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser recruiter = userDetails.getUser();
        UserJob application = userJobRepo.findById(applicationId).orElseThrow();

        if (application.getJob().getPostedBy().getId().equals(recruiter.getId())) {
            application.setStatus(status);
            userJobRepo.save(application);
        }

        return "redirect:/recruiter/applications";
    }

    // Recruiter: Close a Job Posting
    @PostMapping("/close/{jobId}")
    public String closeJob(@PathVariable Long jobId,
                           @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser recruiter = userDetails.getUser();
        Job job = jobRepo.findById(jobId).orElseThrow();

        if (job.getPostedBy().getId().equals(recruiter.getId())) {
            job.setStatus("closed");
            jobRepo.save(job);
        }

        return "redirect:/recruiter/my-posts";
    }
    
    @PostMapping("/delete/{jobId}")
    public String deleteJob(@PathVariable Long jobId,
                            @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser recruiter = userDetails.getUser();
        Job job = jobRepo.findById(jobId).orElseThrow();

        // Only allow deletion if the recruiter owns the job
        if (job.getPostedBy().getId().equals(recruiter.getId())) {
            // Optionally: delete related applications first (to avoid foreign key constraint errors)
            List<UserJob> applications = userJobRepo.findByJob(job);
            userJobRepo.deleteAll(applications);

            jobRepo.delete(job);
        }

        return "redirect:/recruiter/my-posts";
    }

}
