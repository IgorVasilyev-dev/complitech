package by.complitech.demo.util.jwt.api;


import by.complitech.demo.model.RefreshToken;

import java.util.Date;

public interface IJwtUtil<T> {

    RefreshToken generateRefreshToken(T t);

    String generateAccessToken(T t);

    String getUserId(String token);

    String getLogin(String token);

    Date getExpirationDate(String token);

    boolean validate(String token);

}
