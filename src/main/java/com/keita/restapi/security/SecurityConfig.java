package com.keita.restapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //
        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionManageConfig -> sessionManageConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests( auth -> {
                    auth.requestMatchers(HttpMethod.POST, "/user/autheticate").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/user/register").permitAll();
                    auth.requestMatchers("/error").permitAll();
                });

        return http.build();
    }
}
