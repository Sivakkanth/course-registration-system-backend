package com.example.Couse.Registration.and.System.controller;

import com.example.Couse.Registration.and.System.common.APIResponse;
import com.example.Couse.Registration.and.System.dto.LoginRequest;
import com.example.Couse.Registration.and.System.dto.RequestMeta;
import com.example.Couse.Registration.and.System.dto.SignUpRequest;
import com.example.Couse.Registration.and.System.service.LoginService;
import com.example.Couse.Registration.and.System.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RequestMeta requestMeta;

    @PostMapping("/signup")
    public ResponseEntity<APIResponse> signup(@RequestBody SignUpRequest signUpRequest){
        APIResponse apiResponse = loginService.signup(signUpRequest);
        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody LoginRequest loginRequest){
        APIResponse apiResponse = loginService.login(loginRequest);
        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

    @GetMapping("/privateApi")
    public ResponseEntity<APIResponse> privateApi(@RequestHeader(value = "authorization", defaultValue = "") String auth) throws Exception {
        APIResponse apiResponse = new APIResponse();
        jwtUtils.verify(auth);
        apiResponse.setData("Set the verify the data");
        System.out.println(requestMeta.getUserName());
        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }
}