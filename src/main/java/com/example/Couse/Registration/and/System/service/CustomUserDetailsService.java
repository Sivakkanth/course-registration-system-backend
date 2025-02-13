//package com.example.Couse.Registration.and.System.service;
//
//import com.example.Couse.Registration.and.System.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.stream.Collectors;
//
//@Service
//public class CustomUserDetailsService {
//    @Autowired
//    UserRepository userRepository;
//
////    @Override
////    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
////        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: "+username));
////        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles().stream()
////                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
////    }
//}
