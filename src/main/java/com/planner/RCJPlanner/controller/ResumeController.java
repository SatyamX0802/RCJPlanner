package com.planner.RCJPlanner.controller;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.planner.RCJPlanner.entities.*;
import com.planner.RCJPlanner.repository.*;
import com.planner.RCJPlanner.security.AppUserDetails;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired private ResumeRepository resumeRepo;
    @Autowired private AppUserRepository userRepo;

    @GetMapping("/form")
    public String showResumeForm(Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        Resume resume = resumeRepo.findByUser(user).orElse(new Resume());
        resume.setUser(user);

        model.addAttribute("resume", resume);
        return "resume/form";
    }

//    @PostMapping("/submit")
//    public String submitResume(@ModelAttribute Resume resume, @AuthenticationPrincipal AppUserDetails userDetails) {
//        AppUser user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
//        resume.setUser(user);
//
//        resume.getEducationList().forEach(e -> e.setResume(resume));
//        resume.getSkillList().forEach(s -> s.setResume(resume));
//        resume.getExperienceList().forEach(x -> x.setResume(resume));
//        resume.getProjectList().forEach(p -> p.setResume(resume));
//        resume.getCertificateList().forEach(c -> c.setResume(resume));
//
//        resumeRepo.save(resume);
//        return "redirect:/dashboard";
//    }
    
    @PostMapping("/submit")
    public String submitResume(@ModelAttribute Resume resumeForm,
                               @AuthenticationPrincipal AppUserDetails userDetails,
                               Model model) {

        AppUser user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();

        Resume existingResume = resumeRepo.findByUser(user).orElse(new Resume());
        existingResume.setUser(user);
        existingResume.setProfileSummary(resumeForm.getProfileSummary());
        existingResume.setGithubLink(resumeForm.getGithubLink());
        existingResume.setLinkedinLink(resumeForm.getLinkedinLink());

        existingResume.getEducationList().clear();
        existingResume.getEducationList().addAll(resumeForm.getEducationList());
        existingResume.getEducationList().forEach(e -> e.setResume(existingResume));

        existingResume.getSkillList().clear();
        existingResume.getSkillList().addAll(resumeForm.getSkillList());
        existingResume.getSkillList().forEach(s -> s.setResume(existingResume));

        existingResume.getExperienceList().clear();
        existingResume.getExperienceList().addAll(resumeForm.getExperienceList());
        existingResume.getExperienceList().forEach(x -> x.setResume(existingResume));

        existingResume.getProjectList().clear();
        existingResume.getProjectList().addAll(resumeForm.getProjectList());
        existingResume.getProjectList().forEach(p -> p.setResume(existingResume));

        existingResume.getCertificateList().clear();
        existingResume.getCertificateList().addAll(resumeForm.getCertificateList());
        existingResume.getCertificateList().forEach(c -> c.setResume(existingResume));

        resumeRepo.save(existingResume);

        // ✅ Show message on same form page
        model.addAttribute("resume", existingResume);
        model.addAttribute("message", "Resume updated successfully!");

        return "resume/form"; // instead of redirect:/dashboard
    }


    
    @GetMapping("/view")
    public String viewResume(Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        AppUser user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        Resume resume = resumeRepo.findByUser(user).orElse(null);

//        if (resume == null) {
//            return "redirect:/resume/form"; // if resume not found, go to form
//        }

        model.addAttribute("resume", resume);
        return "resume/view";
    }
    @GetMapping("/pdf")
    public void downloadPdf(HttpServletResponse response,
                            @AuthenticationPrincipal AppUserDetails userDetails) throws IOException, DocumentException {
        AppUser user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        Resume resume = resumeRepo.findByUser(user).orElseThrow();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=resume.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // Header
        Paragraph title = new Paragraph(user.getFullName().toUpperCase(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph contact = new Paragraph(
            user.getEmail() + " | " + resume.getGithubLink() + " | " + resume.getLinkedinLink(), normalFont);
        contact.setAlignment(Element.ALIGN_CENTER);
        document.add(contact);
        document.add(Chunk.NEWLINE);

        // Profile Summary
        document.add(new Paragraph("PROFILE SUMMARY", headingFont));
        document.add(new Paragraph(resume.getProfileSummary(), normalFont));
        document.add(Chunk.NEWLINE);

        // Education
        document.add(new Paragraph("EDUCATION", headingFont));
        for (Education e : resume.getEducationList()) {
            document.add(new Paragraph(
                e.getLevel() + " - " + e.getInstitution() + 
                " (" + e.getStartYear() + " - " + e.getEndYear() + ") - " + e.getPercentage(), normalFont));
        }
        document.add(Chunk.NEWLINE);

        // Skills
        document.add(new Paragraph("SKILLS", headingFont));
        for (Skill s : resume.getSkillList()) {
            document.add(new Paragraph("• " + s.getName() + " (" + s.getType() + ")", normalFont));
        }
        document.add(Chunk.NEWLINE);

        // Experience
        document.add(new Paragraph("EXPERIENCE", headingFont));
        for (Experience ex : resume.getExperienceList()) {
            String duration = ex.isCurrentlyWorking() ? "Present" : ex.getEndYear() + "";
            document.add(new Paragraph(
                ex.getJobTitle() + " (" + ex.getStartYear() + " - " + duration + ")", normalFont));
            document.add(new Paragraph("• " + ex.getDescription(), normalFont));
        }
        document.add(Chunk.NEWLINE);

        // Projects
        document.add(new Paragraph("PROJECTS", headingFont));
        for (Project p : resume.getProjectList()) {
            document.add(new Paragraph("• " + p.getTitle(), normalFont));
            document.add(new Paragraph(p.getDescription(), normalFont));
        }
        document.add(Chunk.NEWLINE);

        // Certificates
        document.add(new Paragraph("CERTIFICATES", headingFont));
        for (Certificate c : resume.getCertificateList()) {
            document.add(new Paragraph("• " + c.getTitle() + " - " + c.getUrl(), normalFont));
        }

        document.close();
    }


}
