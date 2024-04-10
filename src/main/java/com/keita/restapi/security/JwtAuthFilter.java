package com.keita.restapi.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.keita.restapi.user.User;
import com.keita.restapi.user.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter to intercept incoming HTTP requests and authenticate them using JWT authentication.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter{

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Method to filter incoming HTTP requests and perform JWT authentication.
     *
     * @param request     The HTTP request object.
     * @param response    The HTTP response object.
     * @param filterChain The filter chain for continuing the request processing.
     * @throws ServletException If an error occurs during request processing.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve the JWT token from the request's Authorization header
        String authHeader = request.getHeader("Authorization");

        // Check if the Authorization header is missing or doesn't start with "Bearer "
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If not, continue with the filter chain
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token (excluding "Bearer ")
        String jwt = authHeader.split(" ")[1];

        // Extract the username from the JWT token
        String username = jwtService.extractUsername(jwt);

        // Retrieve user details from the database based on the username
        User user = userRepository.findByUsername(username).get();

        // Create an authentication token using the extracted username and user authorities
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            username, null, user.getAuthorities()
        );

        // Set the authentication token in the security context
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Continue with the filter chain
        filterChain.doFilter(request, response);

    }

}
