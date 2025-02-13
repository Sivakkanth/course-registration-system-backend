//package com.example.Couse.Registration.and.System.utils;
//
//import com.example.Couse.Registration.and.System.common.AccessDeniedException;
//import com.example.Couse.Registration.and.System.authentication.entities.User;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class JwtUtils {
//    private static final String SECRET_KEY = Base64.getEncoder().encodeToString("This-is-secret-key-must-be-32bytes".getBytes());
//    private static long expirryDuration = 60 * 60 * 1000;
//    private Key getStringKey(){
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String generateJwt(User user){
//        long milliTime = System.currentTimeMillis();
//        Date issueAt = new Date(milliTime);
//        Date expiry = new Date(milliTime + expirryDuration);
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("type", user.getUserType());
//        claims.put("name", user.getName());
//        claims.put("emailId", user.getEmailId());
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(user.getId().toString())
//                .setIssuedAt(issueAt)
//                .setExpiration(expiry)
//                .signWith(getStringKey(), SignatureAlgorithm.HS256)
//                .compact();
//
////        //claims
////        Claims claims = (Claims) Jwts.claims()
////                .setIssuer(user.getId().toString())
////                .setIssuedAt(issueAt)
////                .setExpiration(expiry);
////        claims.put("type", user.getUserType());
////        claims.put("name", user.getName());
////        claims.put("emailId", user.getEmailId());
//
//        //generate jwt using claims
////        return Jwts.builder().setClaims(claims).compact();
//    }
//
////    public Claims verify(String authorization) throws Exception {
////        try {
////            Claims claims = Jwts.parser().setSigningKey(getStringKey()).build().parseClaimsJws(authorization).getBody();
//////            Jwts.parser().setSigningKey(getStringKey()).parseClaimsJws(authorization);
////            System.out.println(claims.get("name"));
////            return claims;
////        } catch (Exception e) {
////            throw new AccessDeniedException("Access Denied");
////        }
////    }
//    public Claims verify(String authorization) throws AccessDeniedException {
//        try {
//            if (authorization == null || !authorization.startsWith("Bearer ")) {
//                throw new AccessDeniedException("Authorization token missing or incorrect format");
//            }
//            String token = authorization.substring(7);  // Removing "Bearer " prefix
//            Claims claims = Jwts.parser().setSigningKey(getStringKey()).build().parseClaimsJws(token).getBody();
//            System.out.println(claims.get("name"));
//            return claims;
//        } catch (JwtException e) {
//            throw new AccessDeniedException("Access Denied");
//        }
//    }
//}
