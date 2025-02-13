package com.example.Couse.Registration.and.System.repository;

import com.example.Couse.Registration.and.System.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

//    @Query("SELECT u FROM User u WHERE (u.emailId = :emailId)")
    Optional<User> findByEmailId(String emailId);

//    @Query("SELECT * FROM user WHERE")
//    User findUser(String username);

    @Query("SELECT u FROM User u WHERE (u.username = :username)")
    User findUser(String username);

    boolean existsByEmailId(String emailId);
}