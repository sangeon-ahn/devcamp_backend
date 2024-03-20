package com.devcamp.auth.entity;

import com.devcamp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column(length = 50)
    private String phone;

    @Column(length = 50)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column
    private String regNo;

    @Column(columnDefinition = "boolean default false")
    private boolean isPersonalInfoVerified;

    //TODO 로그인 구현 시 엑세스 토큰과 리프레시 토근, 로그인 기록 필드 추가
    public User() {}

    public User(String name, String email, String password, String phone, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

}
