package com.hrelix.app.utils;

import com.hrelix.app.models.Employee;
import com.hrelix.app.services.EmployeeService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        UUID userId = null;
        String jwt = null;

        // Extract JWT token from the Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                // Extract userId (UUID) from the token
                userId = UUID.fromString(jwtUtils.extractUsername(jwt));  // Refactored for clarity
            } catch (IllegalArgumentException e) {
                // Handle invalid UUID format gracefully
                System.out.println("Invalid JWT token: " + e.getMessage());
            }
        }

        // If userId is extracted and no authentication is set in the SecurityContext
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Employee userDetails = employeeService.findById(userId);

            // Validate the token with the user's ID
            if (jwtUtils.validateToken(jwt, userDetails.getId().toString())) {

                // Create an authentication token with the user details and set it in the security context
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                System.out.println("Invalid JWT token for user ID: " + userId);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}