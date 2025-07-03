package com.planner.RCJPlanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.planner.RCJPlanner.entities.AppUser;
import com.planner.RCJPlanner.repository.AppUserRepository;

@Controller
public class AuthController {

    @Autowired private AppUserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new AppUser());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AppUser user, Model model) {
    	if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("user", user);
            model.addAttribute("errorMsg", "Username already exists");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/login?registered";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
