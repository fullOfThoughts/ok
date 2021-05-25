package com.bmd.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Token {
    private static final String secret = "secret";

    public static String getToken(Integer Expire) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 60 * Expire);

        return JWT.create().withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(secret));
    }

//    public static String getToken(Map<String, String> map) {
//        JWTCreator.Builder builder = JWT.create();
//        Set<String> strings = map.keySet();
//        strings.forEach(key -> {
//            builder.withClaim(key, map.get(key));
//        });
//        return builder.sign(Algorithm.HMAC256(secret));
//    }

    public static String getToken(Map<String, String> map, Integer Expire) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 60 * Expire);
        JWTCreator.Builder builder = JWT.create();
        Set<String> strings = map.keySet();
        strings.forEach(key -> {
            builder.withClaim(key, map.get(key));
        });
        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(secret));
    }

    public static Boolean checkToken(String token) {
        JWTVerifier build = JWT.require(Algorithm.HMAC256(secret)).build();
        try {
            build.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public static String getUname(String token) {
        JWTVerifier build = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT verify = build.verify(token);
        return verify.getClaim("userName").asString();
    }

}
