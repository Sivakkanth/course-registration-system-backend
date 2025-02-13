package com.example.Couse.Registration.and.System.authentication.controller;

import com.example.Couse.Registration.and.System.authentication.dto.JwtAuthenticationResponse;
import com.example.Couse.Registration.and.System.authentication.dto.RefreshTokenRequest;
import com.example.Couse.Registration.and.System.authentication.entities.User;
import com.example.Couse.Registration.and.System.authentication.entities.otpverification.service.impl.OTPVerficationService;
import com.example.Couse.Registration.and.System.authentication.services.impl.AuthenticationServiceImpl;
import com.example.Couse.Registration.and.System.dto.LoginRequest;
import com.example.Couse.Registration.and.System.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private final AuthenticationServiceImpl authenticationService;
    @Autowired
    private final OTPVerficationService otpVerficationService;

//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest) {
//        // Check if email already exists
//        if (authenticationService.isEmailExists(signUpRequest.getEmailId())) {
//            return ResponseEntity.badRequest().body("Email already exists");
//        }
//
//        // Store user signup data temporarily
//        authenticationService.storeTempSignupData(signUpRequest);
//
//        // Send OTP to email
//        otpVerficationService.sendOTP(signUpRequest.getEmailId());
//
//        return ResponseEntity.ok("OTP sent successfully. Please verify.");
////        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Validated @RequestBody SignUpRequest signUpRequest) {
        try {
            // Check if email already exists
            if (authenticationService.isEmailExists(signUpRequest.getEmailId())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            // Store user signup data temporarily
            authenticationService.storeTempSignupData(signUpRequest);

            // Send OTP to email
            String otp = otpVerficationService.sendOTP(signUpRequest.getEmailId());

            // Return structured response
            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP sent successfully. Please verify.");
            response.put("email", signUpRequest.getEmailId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authenticationService.signin(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            authenticationService.logout(refreshTokenRequest.getToken());
            response.put("message", "User logged out successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Logout failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
