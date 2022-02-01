package by.complitech.demo.service.userService;

import by.complitech.demo.model.RefreshToken;
import by.complitech.demo.model.User;
import by.complitech.demo.service.userService.api.IUserTokenService;
import by.complitech.demo.storage.api.IRefreshTokenRepository;
import by.complitech.demo.util.jwt.api.IJwtUtil;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public class UserTokenService implements IUserTokenService {

    private final IJwtUtil<User> jwtUtil;

    private final IRefreshTokenRepository refreshTokenRepository;

    public UserTokenService(IJwtUtil<User> jwtUtil, IRefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return this.refreshTokenRepository.findByToken(token);
    }

    @Override
    public String generateAccessToken(User user) {
        return this.jwtUtil.generateAccessToken(user);
    }

    @Override
    public RefreshToken generateRefreshToken(User user) {
        return this.jwtUtil.generateRefreshToken(user);
    }

    @Override
    public String getUserId(String token) {
        return this.jwtUtil.getUserId(token);
    }

    @Override
    public Date getExpirationDate(String token) {
        return this.jwtUtil.getExpirationDate(token);
    }

    @Override
    public String getLogin(String token) {
        return this.jwtUtil.getLogin(token);
    }

    @Override
    public boolean validate(String token) {
        return this.jwtUtil.validate(token);
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        return this.refreshTokenRepository.save(jwtUtil.generateRefreshToken(user));
    }

    @Override
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            this.refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signIn request");
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteRefreshTokenByUserId(User user) {
         this.refreshTokenRepository.deleteByUserId(user.getId());
    }
}
