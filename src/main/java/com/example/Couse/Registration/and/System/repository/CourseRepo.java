package com.example.Couse.Registration.and.System.repository;

import com.example.Couse.Registration.and.System.authentication.entities.User;
import com.example.Couse.Registration.and.System.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course, String> {
    @Query("SELECT c FROM Course c WHERE c.courseName IN (SELECT DISTINCT s.courseName FROM Student s WHERE s.emailId = :emailId)")
    List<Course> findByEmail(@Param("emailId") String emailId);
}
