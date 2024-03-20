package com.devcamp.auth.dto;

import com.devcamp.auth.entity.UserRole;
import lombok.Getter;

@Getter
public class CreateUserDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private UserRole userRole = UserRole.USER;
}
