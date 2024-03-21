package com.devcamp.auth.service.impl;

import com.devcamp.auth.entity.TokenBlackList;
import com.devcamp.auth.entity.TokenType;
import com.devcamp.auth.jwt.JwtProvider;
import com.devcamp.auth.repository.TokenBlacklistRepository;
import com.devcamp.auth.service.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final JwtProvider jwtProvider;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    @Override
    public void addToBlackList(String accessToken, String refreshToken) {
        Claims accessClaims = jwtProvider.getUserInfoFromToken(accessToken);
        Claims refreshClaims = jwtProvider.getUserInfoFromToken(refreshToken);

        tokenBlacklistRepository.save(new TokenBlackList(
                accessToken,
                accessClaims.getId(),
                TokenType.ACCESS,
                accessClaims.getExpiration()
        ));

        tokenBlacklistRepository.save(new TokenBlackList(
                refreshToken,
                refreshClaims.getId(),
                TokenType.ACCESS,
                refreshClaims.getExpiration()
        ));
    }

    @Override
    public boolean isTokenBlacklisted(String jti) {
        Optional<TokenBlackList> tokenByJti = tokenBlacklistRepository.findByJti(jti);
        return tokenByJti.isPresent();
    }

    @Override
    public void removeExpiredTokens() {
        List<TokenBlackList> expiredList = tokenBlacklistRepository.findAllByExpiresAtLessThan(new Date());
        tokenBlacklistRepository.deleteAllInBatch(expiredList);
    }
}
