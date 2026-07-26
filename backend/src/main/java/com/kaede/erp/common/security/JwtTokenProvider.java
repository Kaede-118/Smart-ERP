package com.kaede.erp.common.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtTokenProvider {


    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    "kaede-erp-system-secret-key-2026-kaede"
                            .getBytes()
            );



    /**
     * 创建Token
     */
    public String createToken(
            Long userId,
            String username
    ){

        return Jwts.builder()

                .subject(
                        String.valueOf(userId)
                )

                .claim(
                        "username",
                        username
                )

                .issuedAt(
                        new Date()
                )

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60 * 24
                        )
                )

                .signWith(key)

                .compact();

    }



    /**
     * 获取用户ID
     */
    public Long getUserId(
            String token
    ){

        Claims claims =
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();


        return Long.valueOf(
                claims.getSubject()
        );

    }



    /**
     * 校验Token
     */
    public boolean validate(
            String token
    ){

        try {

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);


            return true;

        } catch (Exception e){

            return false;

        }

    }

}