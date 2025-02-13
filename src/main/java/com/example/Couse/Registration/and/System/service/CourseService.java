package com.example.Couse.Registration.and.System.service;

import com.example.Couse.Registration.and.System.authentication.entities.User;
import com.example.Couse.Registration.and.System.authentication.services.impl.JWTServiceImpl;
import com.example.Couse.Registration.and.System.model.Course;
import com.example.Couse.Registration.and.System.model.Student;
import com.example.Couse.Registration.and.System.repository.CourseRepo;
import com.example.Couse.Registration.and.System.repository.StudentRepo;
import com.example.Couse.Registration.and.System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private UserRepository userRepository;
    private final JWTServiceImpl jwtService;

    @Autowired
    StudentRepo studentRepo;

    public CourseService(JWTServiceImpl jwtService) {
        this.jwtService = jwtService;
    }

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    public List<Student> getAllRegisterdStudents() {
        return studentRepo.findAll();
    }

    public void registerCourse(String name, String emailId, String courseName) {
        Student student = new Student(name, emailId, courseName);
        studentRepo.save(student);
    }

    public List<Course> getMyCourses(String emailId) {
        return courseRepo.findByEmail(emailId);

    }

    public User getMyDetails(String token) {
        String username = jwtService.extractUserName(token);
        if (username == null) {
            throw new IllegalArgumentException("Invalid token.");
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }
}
