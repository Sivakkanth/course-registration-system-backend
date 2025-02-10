package com.example.Couse.Registration.and.System.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String password;
    private String name;
    private String gender;
    private String emailId;
    private String phone;
}
