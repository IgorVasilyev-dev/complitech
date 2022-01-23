package by.complitech.demo.service;

import by.complitech.demo.model.RefreshToken;
import by.complitech.demo.model.User;
import by.complitech.demo.storage.api.IRefreshTokenRepository;
import by.complitech.demo.util.jwt.api.IJwtUtil;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public class UserTokenService {

    private final IJwtUtil<User> jwtUtil;

    private final IRefreshTokenRepository refreshTokenRepository;

    public UserTokenService(IJwtUtil<User> jwtUtil, IRefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return this.refreshTokenRepository.findByToken(token);
    }

    public String generateAccessToken(User user) {
        return this.jwtUtil.generateAccessToken(user);
    }

    public RefreshToken generateRefreshToken(User user) {
        return this.jwtUtil.generateRefreshToken(user);
    }

    public String getUserId(String token) {
        return this.jwtUtil.getUserId(token);
    }

    public Date getExpirationDate(String token) {
        return this.jwtUtil.getExpirationDate(token);
    }

    public String getLogin(String token) {
        return this.jwtUtil.getLogin(token);
    }

    public boolean validate(String token) {
        return this.jwtUtil.validate(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        return this.refreshTokenRepository.save(jwtUtil.generateRefreshToken(user));
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            this.refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signIn request");
        }
        return token;
    }

    @Transactional
    public void deleteRefreshTokenByUserId(User user) {
         this.refreshTokenRepository.deleteByUserId(user.getId());
    }
}
