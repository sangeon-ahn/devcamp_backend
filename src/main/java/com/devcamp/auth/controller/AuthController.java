package com.devcamp.auth.controller;

import com.devcamp.auth.dto.CreateUserDto;
import com.devcamp.auth.dto.SignupResponseDto;
import com.devcamp.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody CreateUserDto createUserDto) {
        SignupResponseDto responseDto = userService.signup(createUserDto);
        return ResponseEntity.ok(responseDto);
    }
}
