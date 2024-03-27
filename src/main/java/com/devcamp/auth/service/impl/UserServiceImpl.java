package com.devcamp.auth.service.impl;

import com.devcamp.auth.dto.CreateUserDto;
import com.devcamp.auth.dto.LoginResponseDto;
import com.devcamp.auth.dto.SignupResponseDto;
import com.devcamp.auth.dto.LoginRequestDto;
import com.devcamp.auth.entity.User;
import com.devcamp.auth.jwt.JwtProvider;
import com.devcamp.auth.repository.UserRepository;
import com.devcamp.auth.security.JwtAuthenticationFilter;
import com.devcamp.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public SignupResponseDto signup(CreateUserDto dto) {
        Optional<User> userByEmail = userRepository.findByEmail(dto.getEmail());
        if (userByEmail.isPresent()) {
            throw new RuntimeException(dto.getEmail() + "already exist");
        }

        User createUser = new User(
                dto.getName(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getPhone(),
                dto.getUserRole()
        );

        userRepository.save(createUser);

        return new SignupResponseDto(
                createUser.getId(),
                createUser.getName(),
                createUser.getEmail(),
                createUser.getPhone()
        );
    }
}
