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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.keita.restapi.user.Permission;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //
        http
                // Disable Cross-Site Request Forgery protection
                .csrf(csrfConfig -> csrfConfig.disable())
                // Set session management to STATELESS to make sure no sessions are created
                .sessionManagement(sessionManageConfig -> sessionManageConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure authentication provider and JWT filter
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Configure URL-based authorization
                .authorizeHttpRequests( auth -> {
                    auth.requestMatchers(HttpMethod.POST, "/user/**").permitAll();
                    auth.requestMatchers("/error").permitAll();

                    auth.requestMatchers(HttpMethod.GET, "/item/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/item/**").hasAuthority(Permission.SAVE_ITEMS.name());

                    auth.requestMatchers(HttpMethod.GET, "/inquery/**").hasAuthority(Permission.SAVE_ITEMS.name());
                    auth.requestMatchers(HttpMethod.POST, "/inquery/**").hasAuthority(Permission.READ_ITEMS.name());

                    auth.anyRequest().denyAll();
                });


        return http.build();
    }
}
