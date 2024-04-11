package com.keita.restapi.user;

import org.springframework.web.bind.annotation.RestController;

import com.keita.restapi.authentication.AuthRequest;
import com.keita.restapi.authentication.AuthResponse;
import com.keita.restapi.authentication.AuthService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthService authService;

    /**
     * Method to register a new user.
     *
     * @param requestUser object containing user details
     * @return ResponseEntity containing the authentication response
     */

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody User requestUser) {
        return new ResponseEntity<>(authService.register(requestUser), HttpStatus.CREATED);
    }

    /**
     * Method to authenticate a user.
     *
     * @param authRequest Authentication request containing user credentials.
     * @return ResponseEntity containing the authentication response
     */

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.login(authRequest), HttpStatus.OK);
    }
}
