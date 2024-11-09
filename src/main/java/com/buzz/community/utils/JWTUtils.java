package com.buzz.community.utils;

import com.buzz.community.exception.user.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWTUtils 클래스는 JWT 토큰의 생성, 검증, 파싱과 같은 유틸리티 메서드를 제공합니다.
 */
@Component
public class JWTUtils {


    /**
     * application.properties 에 저장된 JWT 비밀키 값입니다.
     * SecretKey 객체로 변환해 사용할 예정입니다.
     */
    @Value("${jwt.secret}")
    private String secret;


    /**
     * JWT 서명 및 검증에 사용되는 SecretKey 객체입니다.
     * application.properties 에 저장된 값을 init() 을 통해 SecretKey 객체로 변환하여 설정합니다.
     */
    private SecretKey secretKey;


    // TODO @PostConstruct 에 대해 공부하기

    /**
     * 클래스가 초기화될 때 호출됩니다.
     * application.properties 에 저장된 JWT 비밀키를 SecretKey 객체로 변환하여 설정합니다.
     */
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }


    /**
     * 액세스 토큰의 유효 시간 (밀리초 기준)입니다.
     * 1000L * 60 * 60 은 1시간이며 정책상 설정한 유효시간은 24시간입니다.
     */
    public static final long ACCESS_TOKEN_VALIDATION_SECOND = 1000L * 60 * 60 * 24;


    /**
     * 사용자 ID 및 이름을 기반으로 액세스 토큰을 생성하는 메서드입니다.
     *
     * @param userId 사용자 ID
     * @param name 사용자 닉네임
     * @return 생성된 JWT 토큰 문자열
     */
    public String createToken(String userId, String name) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_VALIDATION_SECOND);

        return Jwts.builder()
                .setSubject(userId)
                .claim("name", name)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }


    /**
     * 주어진 JWT 토큰에서 Claim을 추출하고, 토큰의 유효성을 검증합니다.
     * 토큰이 만료되었거나 서명이 잘못된 경우 예외를 던지며, 만료 시간이 임박한 경우 새 토큰을 발급합니다.
     *
     * @param token 유효성을 검사할 JWT 토큰 문자열 (Bearer를 제외한 순수 토큰 문자열)
     * @return 만료 시간이 임박한 경우 새로 발급된 JWT 토큰을 반환하고, 그렇지 않은 경우 null을 반환합니다.
     * @throws InvalidTokenException 토큰이 만료되었거나 서명 오류, 구조 오류 등으로 유효하지 않은 경우 발생합니다.
     */
    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            Date now = new Date();
            long remainingTokenLifetime = expiration.getTime() - now.getTime();
            long tokenRefreshThreshold = 1000L * 60 * 60 * 3;

            if (remainingTokenLifetime <= tokenRefreshThreshold) {
                return createToken(claims.getSubject(), (String) claims.get("name"));
            } else {
                return null;
            }

        } catch (SignatureException e) {
            throw new InvalidTokenException("잘못된 토큰 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("만료된 토큰입니다.");
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("구조가 잘못된 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("토큰이 비어있습니다.");
        }
    }


    /**
     * JWT 토큰에서 사용자 ID를 추출하는 메서드입니다.
     * 단순히 사용자 ID 만을 추출하며 유효성 검증은 하지 않습니다.
     *
     * @param token 사용자 ID를 추출할 JWT 토큰
     * @return 토큰에서 추출된 사용자 ID (subject 필드 값)
     */
    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
