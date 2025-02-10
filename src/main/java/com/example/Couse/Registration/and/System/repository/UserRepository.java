package com.example.Couse.Registration.and.System.repository;

import com.example.Couse.Registration.and.System.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE (u.emailId = :emailId OR u.username = :username) AND u.password = :password")
    User findByUsernameOrEmailWithPassword(String emailId, String username, String password);
}