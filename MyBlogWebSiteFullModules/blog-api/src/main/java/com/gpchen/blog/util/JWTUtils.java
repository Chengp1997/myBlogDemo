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
        //设置传送的payload，把userId包装成Map对象，并进行设置
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);

        //codec
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，秘钥为jwtToken
                .setClaims(claims) // data to be hashed
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
