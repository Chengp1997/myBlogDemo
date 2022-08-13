package com.gpchen.blog.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    //secret key
    private static final String jwtToken = "123456Mszlu!@###$$";

    //generate token
    public static String createToken(Long userId){
        //set payload
        Map<String,Object> payload = new HashMap<>();
        payload.put("userId",userId);

        //codec
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) // hash algorithm
                .setClaims(payload) // data to be hashed
                .setIssuedAt(new Date()) // issue date
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));// expire time
        String token = jwtBuilder.compact();
        return token;
    }

    //check token
    public static Map<String, Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
