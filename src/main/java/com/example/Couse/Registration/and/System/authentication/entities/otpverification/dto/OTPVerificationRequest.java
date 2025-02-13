package com.example.Couse.Registration.and.System.authentication.entities.otpverification.dto;

import lombok.Data;

@Data
public class OTPVerificationRequest {
    private String mail;
    private String otp;
}
