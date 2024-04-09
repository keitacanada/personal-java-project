package com.keita.restapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests( authConfig -> {
                    authConfig.requestMatchers(HttpMethod.GET).permitAll();
                    authConfig.anyRequest().authenticated();
                });


        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
            .username("admin")
            .password("admin-password")
            .roles("ADMIN")
            .build();
        UserDetails user = User.builder()
            .username("user")
            .password("user-password")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
