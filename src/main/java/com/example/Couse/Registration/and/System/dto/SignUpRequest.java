package com.example.Couse.Registration.and.System.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SignUpRequest {
    private String username;
    private String password;
    private String emailId;
    private Date createAt;
}
