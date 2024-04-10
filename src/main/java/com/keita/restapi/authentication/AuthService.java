package com.keita.restapi.authentication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.keita.restapi.security.JwtService;
import com.keita.restapi.user.User;
import com.keita.restapi.user.UserRepository;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    /**
     * Method to authenticate user login and generate JWT Token.
     *
     * @param Authentication Request containing user credentials.
     * @return Response containing JWT Token.
    */

    public AuthResponse login(AuthRequest authRequest) {
        // Create authentication token with username and password
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()
        );

        authManager.authenticate(authToken);

        User user = userRepository.findByUsername(authRequest.getUsername()).get();

        String jwt = jwtService.generateToken(user, generateClaims(user));

        return new AuthResponse(jwt);
    }

    /**
     * Method to get a user and generate claims for JWT token.
     *
     * @param User object containing user details
     * @return Map containing claims for JWT token
     */

    private Map<String, Object> generateClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getUsername());
        claims.put("role", user.getRole().name());

        return claims;
    }
}
