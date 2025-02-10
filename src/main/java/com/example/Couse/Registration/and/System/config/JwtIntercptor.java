package com.example.Couse.Registration.and.System.config;

import com.example.Couse.Registration.and.System.dto.RequestMeta;
import com.example.Couse.Registration.and.System.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtIntercptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RequestMeta requestMeta;

    public JwtIntercptor(RequestMeta requestMeta){
        this.requestMeta = requestMeta;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String auth = request.getHeader("authorization");

        if( !(request.getRequestURI().contains("login") || request.getRequestURI().contains("signup")  || request.getRequestURI().contains("courses"))){
            Claims claims = jwtUtils.verify(auth);

            requestMeta.setUserName(claims.get("name").toString());
            requestMeta.setUserId(Long.valueOf(claims.getIssuer()));
            requestMeta.setUserType(claims.get("type").toString());
            requestMeta.setEmailId(claims.get("emailId").toString());
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}