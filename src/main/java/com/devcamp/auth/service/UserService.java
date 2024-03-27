package com.devcamp.auth.service;

import com.devcamp.auth.dto.CreateUserDto;
import com.devcamp.auth.dto.LoginResponseDto;
import com.devcamp.auth.dto.SignupResponseDto;
import com.devcamp.auth.dto.LoginRequestDto;

public interface UserService {
    /**
     *  회원가입
     * @param createUserDto
     * 필요 인자: name, email, password, phone
     * @return SignupResponseDto
     */

    SignupResponseDto signup(CreateUserDto createUserDto);
}
