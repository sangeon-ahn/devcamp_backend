package com.devcamp.auth.service;

public interface TokenBlacklistService {
    /**
     * 로그아웃 시 AccessToken, RefreshToken Blacklist 등록
     * @param accessToken
     * @param refreshToken
     */
    void addToBlackList(String accessToken, String refreshToken);

    /**
     * 해당 Token이 Blacklist에 존재하는지 확인.
     * @param jti : JWT ID
     * @return boolean
     */
    boolean isTokenBlacklisted(String jti);

    /**
     * 현재 Date 기준으로 Expired 된 Token들을 Blacklist에서 제거
     */
    void removeExpiredTokens();
}
