package com.devcamp.auth.service;

public interface AuthService {
    /**
     * Refresh Token의 유효성을 체크한 후, Access Token을 발급함.
     * @param refreshToken ; Http Header에서 추출한 Refresh Token
     * @return Access Token
     */
    String refreshAccessToken(String refreshToken);
}
