package com.example.Couse.Registration.and.System.authentication.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String emailId;
    private Date createdAt;

    public User(String username, String password, String emailId, Date date) {
    }

//    @PrePersist
//    public void onSave(){
//        //Create at and update at
//        Date currentDate = new Date();
//
//        this.craeteAt = currentDate;
//        this.updateAt = currentDate;
//    }
//
//    @PostPersist
//    public void onUpdate(){
//        Date currentDate = new Date();
//        this.updateAt = currentDate;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(()->getUsername());
    }

    @Override
    public String getPassword() { return password;}

    @Override
    public String getUsername(){ return username; }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}