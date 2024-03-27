package com.devcamp.auth.controller;

import com.devcamp.auth.dto.CreateUserDto;
import com.devcamp.auth.dto.LoginResponseDto;
import com.devcamp.auth.dto.SignupResponseDto;
import com.devcamp.auth.dto.LoginRequestDto;
import com.devcamp.auth.entity.TokenType;
import com.devcamp.auth.jwt.JwtProvider;
import com.devcamp.auth.service.AuthService;
import com.devcamp.auth.service.TokenBlacklistService;
import com.devcamp.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody CreateUserDto createUserDto) {
        SignupResponseDto responseDto = userService.signup(createUserDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginRequestDto> login() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        tokenBlacklistService.addToBlackList(
                jwtProvider.getJwtFromHeader(request, TokenType.ACCESS),
                jwtProvider.getJwtFromHeader(request, TokenType.REFRESH)
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Token Refresh")
    @GetMapping("/token/new")
    public ResponseEntity<String> refresh(HttpServletRequest request) {
        String accessToken = authService.refreshAccessToken(jwtProvider.getJwtFromHeader(request, TokenType.REFRESH));
        return ResponseEntity.ok((accessToken));
    }

    @Operation(summary = "Token BlackList 초기화")
    @DeleteMapping("/blacklist/expired")
    public ResponseEntity<Void> deleteExpiredTokens() {
        tokenBlacklistService.removeExpiredTokens();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
