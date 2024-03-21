package com.devcamp.auth.service.impl;

import com.devcamp.auth.entity.TokenType;
import com.devcamp.auth.entity.User;
import com.devcamp.auth.jwt.JwtProvider;
import com.devcamp.auth.repository.UserRepository;
import com.devcamp.auth.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    @Override
    public String refreshAccessToken(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Token Refresh Failed: Invalid Token");
        }

        Claims userInfo = jwtProvider.getUserInfoFromToken(refreshToken);

        User user = userRepository.findByEmail(userInfo.getSubject()).orElseThrow(
                () -> new RuntimeException("Not Found User By : " + userInfo.getSubject())
        );

        return jwtProvider.createToken(jwtProvider.createTokenPayLoad(user.getEmail(), TokenType.ACCESS));
    }
}
