package com.example.Couse.Registration.and.System.service;

import com.example.Couse.Registration.and.System.model.Course;
import com.example.Couse.Registration.and.System.model.Student;
import com.example.Couse.Registration.and.System.repository.CourseRepo;
import com.example.Couse.Registration.and.System.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    CourseRepo courseRepo;

    @Autowired
    StudentRepo studentRepo;

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
}
