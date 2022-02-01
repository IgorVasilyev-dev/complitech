package by.complitech.demo.service.userService.api;

import by.complitech.demo.model.RefreshToken;
import by.complitech.demo.model.User;

import java.util.Date;
import java.util.Optional;

public interface IUserTokenService {

    Optional<RefreshToken> findByToken(String token);

    String generateAccessToken(User user);

    RefreshToken generateRefreshToken(User user);

    String getUserId(String token);

    Date getExpirationDate(String token);

    String getLogin(String token);

    boolean validate(String token);

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);

    void deleteRefreshTokenByUserId(User user);
}
