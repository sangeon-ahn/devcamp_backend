package com.devcamp.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}
