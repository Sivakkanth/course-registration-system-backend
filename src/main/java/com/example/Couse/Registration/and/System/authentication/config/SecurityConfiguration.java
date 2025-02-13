package com.example.Couse.Registration.and.System.authentication.config;

import com.example.Couse.Registration.and.System.authentication.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests(request -> request
                    .requestMatchers("/api/v1/auth/logout").authenticated()
//                    .requestMatchers("/courses/registered").permitAll()
//                    .requestMatchers("/courses").permitAll()
//                    .requestMatchers("api/v1/otp/**").permitAll()
                    .requestMatchers("/mydetails").authenticated()
                    .requestMatchers("/mycourses").authenticated()
                    .requestMatchers("/course/apply").authenticated()
                    .anyRequest().permitAll()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors->cors.configurationSource(corsConfigurationSource()));
        return  httpSecurity.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return  new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(Arrays.asList(   // The setAllowedOrigins method specifies which origins (i.e., domains) are allowed to access your backend.
                        "https://course-registration-system-frontend.vercel.app",
                        "http://localhost:3000", // Backend accessible for React
                        "http://localhost:5173", // Backend accessible for Vite
                        "http://localhost:4200"  // Backend accessible for Angular
                ));

                cfg.setAllowedMethods(Collections.singletonList("*"));           // Allowed all HTTP Methods (POST, PUT, DELETE, GET, ...)
                cfg.setAllowCredentials(true);                                   // allowing the browser to send cookies or authentication headers with cross-origin requests.
                cfg.setAllowedHeaders(Collections.singletonList("*"));           // This allows all headers in requests by specifying * as the wildcard.
                cfg.addExposedHeader(Arrays.asList("Authorization").toString()); // This is important if your frontend needs to read custom headers returned by the backend.
                cfg.setMaxAge(3600L);                                            // This sets the maximum age (in seconds) that the CORS response should be cached by the client. In this case, it's set to 3600 seconds (1 hour).

                return cfg;
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
