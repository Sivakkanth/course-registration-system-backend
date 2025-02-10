package com.example.Couse.Registration.and.System.service;

import com.example.Couse.Registration.and.System.common.APIResponse;
import com.example.Couse.Registration.and.System.dto.LoginRequest;
import com.example.Couse.Registration.and.System.dto.SignUpRequest;
import com.example.Couse.Registration.and.System.model.User;
import com.example.Couse.Registration.and.System.repository.UserRepository;
import com.example.Couse.Registration.and.System.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public APIResponse signup(SignUpRequest signUpRequest) {
        APIResponse apiResponse = new APIResponse();

        // validation

        // dto to entity
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setName(signUpRequest.getName());
        user.setGender(signUpRequest.getGender());
        user.setEmailId(signUpRequest.getEmailId());
        user.setPhone(signUpRequest.getPhone());
//        user.setIsActive(Boolean.TRUE);

        // store entity
        user = userRepository.save(user);

        //return
        apiResponse.setData("User create successfully "+user);
        return apiResponse;
    }

    public APIResponse login(LoginRequest loginRequest) {
        APIResponse apiResponse = new APIResponse();

        //validation

        //varify user exist with given email or password
        User user = userRepository.findByUsernameOrEmailWithPassword(loginRequest.getEmailId(), loginRequest.getUsername(), loginRequest.getPassword());

        //response
        if (user == null) {
            apiResponse.setData("User login failed");
            return apiResponse;
        }
        String token = jwtUtils.generateJwt(user);
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", token);
        data.put("id", user.getId());
        data.put("name", user.getName());

        apiResponse.setData(data);
        return apiResponse;
    }
}
