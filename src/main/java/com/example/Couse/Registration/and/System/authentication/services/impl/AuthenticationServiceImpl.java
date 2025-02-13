package com.example.Couse.Registration.and.System.authentication.services.impl;

import com.example.Couse.Registration.and.System.authentication.dto.JwtAuthenticationResponse;
import com.example.Couse.Registration.and.System.authentication.dto.RefreshTokenRequest;
import com.example.Couse.Registration.and.System.authentication.entities.User;
import com.example.Couse.Registration.and.System.dto.LoginRequest;
import com.example.Couse.Registration.and.System.dto.SignUpRequest;
import com.example.Couse.Registration.and.System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTServiceImpl jwtService;
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

//    public User signup(SignUpRequest signUpRequest) {
//        Optional<User> existUserByUserName = userRepository.findByUsername(signUpRequest.getUsername());
//        if (existUserByUserName.isPresent()) throw new RuntimeException("The UserName Already Exist.");
//
//        User user = new User();
//
//        user.setUsername(signUpRequest.getUsername());
//        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
//        user.setEmailId(signUpRequest.getEmailId());
//        user.setCreatedAt(new Date());
//        return userRepository.save(user);
//    }

    public JwtAuthenticationResponse signin(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Username or password"));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("username", user.getUsername());
        extraClaims.put("emailId", user.getEmailId());
        extraClaims.put("id", user.getId());

        var jwt = jwtService.generateToken(user, extraClaims);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String token = refreshTokenRequest.getToken();

        // Check if the token is blacklisted
        if (tokenBlacklist.contains(token)) {
            throw new IllegalArgumentException("Token has been revoked. Please log in again.");
        }

        String userName = jwtService.extractUserName(token);
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new IllegalArgumentException("Invalid Username or password"));

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("username", user.getUsername());
            extraClaims.put("emailId", user.getEmailId());
            extraClaims.put("id", user.getId());

            var jwt = jwtService.generateToken(user, extraClaims);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        throw new IllegalArgumentException("Invalid refresh token.");
    }

    public boolean isEmailExists(String emailId) {
        return userRepository.existsByEmailId(emailId);
    }

    private final Map<String, SignUpRequest> tempUserStorage = new ConcurrentHashMap<>();
    public void storeTempSignupData(SignUpRequest signUpRequest) {
        tempUserStorage.put(signUpRequest.getEmailId(), signUpRequest);
    }

    public void completeSignup(String mail) {
        SignUpRequest request = tempUserStorage.get(mail);
        if (request == null) {
            throw new RuntimeException("User data not found for email: " + mail);
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Create and save the user
        User newUser = new User();
        newUser.setEmailId(request.getEmailId());
        newUser.setUsername(request.getUsername());
        newUser.setCreatedAt(new Date());
        newUser.setPassword(hashedPassword); // Save hashed password
        userRepository.save(newUser);

        // Remove temporary data
        tempUserStorage.remove(mail);
    }

    public void logout(String token) {
        if (token != null) {
            // Check if the token is valid
            if (!jwtService.isTokenValid(token, userRepository.findByUsername(jwtService.extractUserName(token)).orElseThrow(() -> new IllegalArgumentException("Invalid Username or password")))) {
                throw new IllegalArgumentException("Invalid token. Cannot logout.");
            }

            // Check if the token is already blacklisted
            if (tokenBlacklist.contains(token)) {
                throw new IllegalArgumentException("Token has already been revoked.");
            }

            tokenBlacklist.add(token); // Store the token as invalid
            System.out.println("Logout successful.");
        }
    }
}
