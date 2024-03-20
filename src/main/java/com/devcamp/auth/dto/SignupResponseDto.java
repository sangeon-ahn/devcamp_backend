package com.devcamp.auth.dto;

import lombok.Getter;

@Getter
public class SignupResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;

    public SignupResponseDto(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
