package com.example.Couse.Registration.and.System.authentication.entities.otpverification.controller;

import com.example.Couse.Registration.and.System.authentication.entities.otpverification.dto.OTPRequest;
import com.example.Couse.Registration.and.System.authentication.entities.otpverification.dto.OTPVerificationRequest;
import com.example.Couse.Registration.and.System.authentication.entities.otpverification.service.impl.OTPVerficationService;
import com.example.Couse.Registration.and.System.authentication.services.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
public class OTPVerificationController {
    @Autowired
    private final OTPVerficationService otpVerificationService;
    @Autowired
    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/resendotp")
    public ResponseEntity<String> sendOTP(@RequestBody OTPRequest otpRequest) {
        try {
            String responseMessage = otpVerificationService.sendOTP(otpRequest.getOtpSendEmail());
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to resend OTP: " + e.getMessage());
        }
    }

    @PostMapping("/otpverification")
    public ResponseEntity<String> verifyOTP(@RequestBody OTPVerificationRequest otpVerificationRequest) {
//        return ResponseEntity.ok(otpVerificationService.otpVerification(
//                otpVerificationRequest.getMail(),
//                otpVerificationRequest.getOtp()
//        ));
//        boolean isOtpValid = otpVerificationService.otpVerification(
//                otpVerificationRequest.getMail(),
//                otpVerificationRequest.getOtp()
//        );
//
//        if (!isOtpValid) {
//            return ResponseEntity.badRequest().body("Invalid OTP. Try again.");
//        }
//
//        // Save user data after successful OTP verification
//        authenticationService.completeSignup(otpVerificationRequest.getMail());
//
//        return ResponseEntity.ok("Signup successful. Redirecting to home.");
//    }
        try {
            // Verify OTP
            boolean isOtpValid = otpVerificationService.otpVerification(
                    otpVerificationRequest.getMail(),
                    otpVerificationRequest.getOtp()
            );

            if (!isOtpValid) {
                return ResponseEntity.badRequest().body("Invalid OTP. Please try again.");
            }

            // Complete user signup after successful OTP verification
            authenticationService.completeSignup(otpVerificationRequest.getMail());

            return ResponseEntity.ok("OTP verified successfully. User registration completed.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("OTP verification failed: " + e.getMessage());
        }
    }
}
