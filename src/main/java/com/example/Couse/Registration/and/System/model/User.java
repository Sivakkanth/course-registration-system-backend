package com.example.Couse.Registration.and.System.model;

import com.example.Couse.Registration.and.System.utils.Constant;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String emailId;
    private String phone;
    private String userType = Constant.USER_TYPE.NORMAL;
    private Boolean isActive = true;
    private Integer loginCount = 0;
    private String ssoType;
    private Date loginAt;
    private Date craeteAt;
    private Date updateAt;

    public User(Long id, String username, String password, String name, String gender, String emailId, String phone, String userType, Boolean isActive, Integer loginCount, String ssoType, Date loginAt, Date craeteAt, Date updateAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.emailId = emailId;
        this.phone = phone;
        this.userType = userType;
        this.isActive = isActive;
        this.loginCount = loginCount;
        this.ssoType = ssoType;
        this.loginAt = loginAt;
        this.craeteAt = craeteAt;
        this.updateAt = updateAt;
    }

    @PrePersist
    public void onSave(){
        //Create at and update at
        Date currentDate = new Date();

        this.craeteAt = currentDate;
        this.updateAt = currentDate;
    }

    @PostPersist
    public void onUpdate(){
        Date currentDate = new Date();
        this.updateAt = currentDate;
    }

    public User(){

    }
;}
