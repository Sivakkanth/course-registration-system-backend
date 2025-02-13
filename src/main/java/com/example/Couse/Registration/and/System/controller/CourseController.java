package com.example.Couse.Registration.and.System.controller;

import com.example.Couse.Registration.and.System.authentication.entities.User;
import com.example.Couse.Registration.and.System.model.Course;
import com.example.Couse.Registration.and.System.model.Student;
import com.example.Couse.Registration.and.System.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

    @GetMapping("/courses/registered")
    public List<Student> getAllRegisterdStudents(){
        return courseService.getAllRegisterdStudents();
    }

    @PostMapping("/course/apply")
    public String registerCourse(@RequestParam("name") String name,
                                 @RequestParam("emailId") String emailId,
                                 @RequestParam("courseName") String courseName){
        courseService.registerCourse(name, emailId, courseName);
        return "Congratulations! "+name+" Enrollment Successful for "+courseName;
    }

    @GetMapping("/mycourses")
    public List<Course> getMyCourse(@RequestParam("emailId") String emailId){
        return courseService.getMyCourses(emailId);
    }

    @GetMapping("/mydetails")
    public User getMyDetails(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization token.");
        }

        // Pass token without "Bearer " prefix
        return courseService.getMyDetails(token.substring(7));
    }
}
