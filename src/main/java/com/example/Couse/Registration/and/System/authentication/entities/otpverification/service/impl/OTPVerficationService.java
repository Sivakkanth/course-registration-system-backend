package com.example.Couse.Registration.and.System.authentication.entities.otpverification.service.impl;

import com.example.Couse.Registration.and.System.authentication.entities.OTP;
import com.example.Couse.Registration.and.System.repository.OTPRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
@Component
public class OTPVerficationService {
    private final OTPRepository otpRepository;
    private final JavaMailSender javaMailSender;

    public String sendOTP(String email){
        // Validate email
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }

        // Generate a new OTP
        String otp = generateOTP();

        OTP newOTP = otpRepository.findByEmailId(email).orElse(new OTP());
        newOTP.setEmailId(email);
        newOTP.setOtp(otp);
        newOTP.setCreationTime(LocalDateTime.now());
        otpRepository.save(newOTP);

//        if(existEmailOtp.isPresent()) {
//            newOTP = existEmailOtp.get();
//        } else {
//            newOTP = new OTP();
//            newOTP.setEmailId(email);
//        }
//        newOTP.setCreationTime(LocalDateTime.now());
//        newOTP.setOtp(generateOTP());
//        otpRepository.save(newOTP);
//        sendEmail(newOTP.getEmailId(), "OTP Verfication", "The OTP is: "+ newOTP.getOtp());
//        return newOTP.getOtp();
        // Send OTP via email
        try {
            sendEmail(email, "OTP Verification", "Your OTP is: " + otp);
        } catch (Exception e) {
            // Rollback OTP save if email fails to send
            otpRepository.delete(newOTP);
            throw new RuntimeException("Failed to send OTP: " + e.getMessage());
        }

        return otp;
    }

    public void sendEmail(String toEmail, String subject, String body){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("asivabkanth@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Failed To Send An Email To "+toEmail+".");
        }
    }

    public String generateOTP(){
        Random random = new Random();
        int randomNumber =  random.nextInt(9999);
        String otp = Integer.toString(randomNumber);
        while(otp.length() < 4 ){
            otp = "0" + otp;
        }
        return otp;
    }

    public boolean otpVerification(String studentMail, String otp){
        Optional<OTP> existStudentOTP = otpRepository.findByEmailId(studentMail);
        if (!existStudentOTP.isPresent()){
            throw new RuntimeException("The Email "+studentMail+" Is Not Valid.");
        }
        if (existStudentOTP.get().getOtp() == null){
            throw new RuntimeException("There Is No Any OTP Found For This Email "+studentMail);
        }
        boolean isOtpValid = existStudentOTP.filter(value -> Objects.equals(otp, value.getOtp())).isPresent();
        if (!isOtpValid){
            throw new RuntimeException("The OTP Is Not Match.");
        }
        if (isOtpValid && !existStudentOTP.get().getCreationTime().isAfter(LocalDateTime.now().minusMinutes(3))){
            throw new RuntimeException("The OTP Is Expired.");
        }
//        return existStudentOTP.filter(value -> Objects.equals(otp, value.getOtp())).isPresent();
        existStudentOTP.get().setOtp(null);
        otpRepository.save(existStudentOTP.get());
        return isOtpValid;
    }

    private boolean isValidEmail(String email) {
        // Simple email validation (you can use a regex or a library like Apache Commons Validator)
        return email != null && email.contains("@");
    }

    @Scheduled(fixedRate = 60000)
    public void deleteExpiredOTPs(){
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(3);
        otpRepository.deleteByCreationTimeBefore(oneMinuteAgo);
    }
}
