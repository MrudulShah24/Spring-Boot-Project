package com.libb.booknest2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.libb.booknest2.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                // Public registration and login
                .requestMatchers("/api/auth/**").permitAll()

                // Public GET access for books and authors (search, view)
                .requestMatchers(HttpMethod.GET, "/api/books/**", "/api/authors/**").permitAll()

                // Admin-only access
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/books/**", "/api/authors/**").hasRole("ADMIN")

                // Admin & Librarian can add/update
                .requestMatchers(HttpMethod.POST, "/api/books/**", "/api/authors/**").hasAnyRole("ADMIN", "LIBRARIAN")
                .requestMatchers(HttpMethod.PUT, "/api/books/**", "/api/authors/**").hasAnyRole("ADMIN", "LIBRARIAN")

                // Authenticated users can access loans
                .requestMatchers(HttpMethod.GET, "/api/loans").hasAnyRole("LIBRARIAN", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/loans").authenticated()  // POST /api/loans should be accessible only by authenticated users 
                .requestMatchers(HttpMethod.PUT, "/api/loans/*/return").hasAnyRole("LIBRARIAN", "ADMIN")

                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .userDetailsService(userDetailsService)
            .httpBasic(); 

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
