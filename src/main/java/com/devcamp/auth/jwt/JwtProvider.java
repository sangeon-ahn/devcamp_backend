package com.devcamp.auth.jwt;

import com.devcamp.auth.entity.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtProvider {

    // 엑세스 토큰의 헤더 key값
    public static final String ACCESS_TOKEN_HEADER = "AccessToken";

    // 리프레시 토큰의 헤더 key값
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";

    // 토큰의 value값의 prefix
    public static final String BEARER_PREFIX = "Bearer ";

    // 액세스 토큰 수명
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L;

    // 리프레시 토큰 수명
    private final long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 7; // 7일

    @Value("${jwt.secret.key}")
    private String secretKey; // BASE64로 암호화된 키값
    private SecretKey key; // BASE64로 암호화된 secretKey를 복호화하여 만든 비밀키
    private MacAlgorithm algorithm;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        algorithm = Jwts.SIG.HS256;
    }

    /**
     * 토큰 정보를 담는 payload 생성
     * @param email : 사용자 고유 식별값(email)
     * @param tokenType : ACCESS | REFRESH
     * @return TokenPayLoad
     */
    public TokenPayLoad createTokenPayLoad(String email, TokenType tokenType) {
        Date date = new Date();
        long tokenTime = tokenType.equals(TokenType.ACCESS) ? ACCESS_TOKEN_TIME : REFRESH_TOKEN_TIME;

        return new TokenPayLoad(
                email, // sub
                UUID.randomUUID().toString(), // jti
                date, // iat
                new Date(date.getTime() + tokenTime)
        );
    }

    /**
     * 토큰 생성
     * @param payLoad : 토큰 생성을 위한 정보 인스턴스
     * @return JWT(토큰)
     */
    public String createToken(TokenPayLoad payLoad) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(payLoad.getSub()) // 사용자 식별용 이메일
                        .expiration(payLoad.getExpiresAt()) // 밀리초 단위 만기 시간
                        .issuedAt(payLoad.getIat()) // 발급(issue)시간
                        .signWith(key, algorithm) // 암호키 & 알고리즘
                        .compact();
    }

    /**
     * HTTP Header 에서 JWT 추출
     * @param request : HTTP Request 정보
     * @param tokenType : 추출할 토큰 타입
     * @return Header에서 추출한 JWT
     */
    public String getJwtFromHeader(HttpServletRequest request, TokenType tokenType) {
        String bearerToken = request.getHeader(TokenType.ACCESS.equals(tokenType) ?
                ACCESS_TOKEN_HEADER : REFRESH_TOKEN_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    /**
     * 토큰의 유효성 검사
     * @param token : JWT
     * @return boolean
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid Token: " + e.getMessage());
        }
        return false;
    }

    /**
     * JWT 내부에 저장한 subject, expiration, issuedAt, id(jti) 정보를 담은 Claims 추출
     * @param token : JWT
     * @return Claims
     */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
