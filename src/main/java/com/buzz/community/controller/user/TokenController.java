package com.buzz.community.controller.user;

import com.buzz.community.exception.user.InvalidTokenException;
import com.buzz.community.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


/**
 * 토큰 검증을 담당하는 컨트롤러 클래스입니다.
 * 클라이언트로부터 전달된 JWT 토큰을 검증하고, 만료가 임박하면 새 토큰을 발급합니다.
 */
@Slf4j
@RestController
public class TokenController {

    private final JWTUtils jwt;

    public TokenController(JWTUtils jwt) {
        this.jwt = jwt;
    }


    /**
     * JWT 토큰을 검증하는 엔드포인트입니다.
     * 클라이언트의 요청 헤더에서 토큰을 추출하고, 유효성을 확인합니다.
     * 만약 토큰이 유효하지 않거나 만료가 임박하면 새 토큰을 발급합니다.
     *
     * @param tokenHeader 클라이언트의 Authorization 헤더에서 전달된 JWT 토큰
     * @return 토큰 검증 결과와 새로운 토큰이 있으면 헤더에 추가하여 반환합니다.
     */
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : null;

        if (token == null) {
            throw new InvalidTokenException("토큰이 제공되지 않았습니다.");

        } else {
            String newToken = jwt.validateToken(token);
            if (newToken != null) {
                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + newToken)
                        .body(true);
            }

            return ResponseEntity.ok(true);
        }
    }
}
