package com.example.Couse.Registration.and.System.repository;

import com.example.Couse.Registration.and.System.authentication.entities.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Integer> {
    Optional<OTP> findByEmailId(String emailId);
    Optional<OTP> findByOtp(String otp);
    void deleteByCreationTimeBefore(LocalDateTime time);
}
