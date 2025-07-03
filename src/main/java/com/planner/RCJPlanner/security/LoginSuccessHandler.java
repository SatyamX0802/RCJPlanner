// com/planner/RCJPlanner/security/LoginSuccessHandler.java
package com.planner.RCJPlanner.security;

import com.planner.RCJPlanner.entities.AppUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        AppUser user = userDetails.getUser();

        if ("RECRUITER".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("/recruiter/dashboard");
        } else {
            response.sendRedirect("/dashboard");
        }
    }
}
