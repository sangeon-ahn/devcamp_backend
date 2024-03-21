package com.devcamp.auth.entity;

import com.devcamp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
public class AccessToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String jti;

    @Column
    private String token;

    @Column
    private Date expiresAt;

    @Column(columnDefinition = "boolean default false")
    private boolean isRevoke;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
