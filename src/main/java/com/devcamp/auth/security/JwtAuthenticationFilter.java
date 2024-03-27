package com.devcamp.auth.security;

import com.devcamp.auth.dto.LoginRequestDto;
import com.devcamp.auth.entity.AccessLog;
import com.devcamp.auth.entity.TokenType;
import com.devcamp.auth.entity.User;
import com.devcamp.auth.jwt.JwtProvider;
import com.devcamp.auth.repository.AccessLogRepository;
import com.devcamp.common.util.HttpRequestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;
    private final AccessLogRepository accessLogRepository;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, AccessLogRepository accessLogRepository) {
        this.jwtProvider = jwtProvider;
        this.accessLogRepository = accessLogRepository;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 로그인 성공시 호출된다.
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * method.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        User user = ((UserDetailsImpl)authResult.getPrincipal()).getUser();
        String email = user.getEmail();
        String accessToken = jwtProvider.createToken(jwtProvider.createTokenPayLoad(email, TokenType.ACCESS));
        String refreshToken = jwtProvider.createToken(jwtProvider.createTokenPayLoad(email, TokenType.REFRESH));

        response.addHeader(JwtProvider.ACCESS_TOKEN_HEADER, accessToken);
        response.addHeader(JwtProvider.REFRESH_TOKEN_HEADER, refreshToken);

        AccessLog accessLog = new AccessLog(
                HttpRequestUtils.getUserAgent(request),
                request.getRequestURI(),
                HttpRequestUtils.getRemoteAddr(request),
                user
        );

        accessLogRepository.save(accessLog);
    }

    /**
     * 로그인 실패시 호출된다.
     * @param request
     * @param response
     * @param failed
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
