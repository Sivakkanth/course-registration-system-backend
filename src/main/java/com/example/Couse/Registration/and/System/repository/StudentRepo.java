package com.example.Couse.Registration.and.System.repository;

import com.example.Couse.Registration.and.System.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {
}
