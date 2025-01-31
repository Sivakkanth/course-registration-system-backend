package com.example.Couse.Registration.and.System.controller;

import com.example.Couse.Registration.and.System.model.Course;
import com.example.Couse.Registration.and.System.model.Student;
import com.example.Couse.Registration.and.System.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://course-registration-system-frontend.vercel.app")
public class CourseController {
    @Autowired
    CourseService courseService;

    @GetMapping("/courses")
    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

    @GetMapping("/courses/enrolled")
    public List<Student> getAllRegisterdStudents(){
        return courseService.getAllRegisterdStudents();
    }

    @PostMapping("/courses/enrolled")
    public String registerCourse(@RequestParam("name") String name,
                                 @RequestParam("emailId") String emailId,
                                 @RequestParam("courseName") String courseName){
        courseService.registerCourse(name, emailId, courseName);
        return "Congratulations! "+name+" Entrollment Successful for "+courseName;
    }
}
