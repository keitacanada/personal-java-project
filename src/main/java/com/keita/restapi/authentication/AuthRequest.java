package com.keita.restapi.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AuthRequest {

    private String username;

    private String password;
}
