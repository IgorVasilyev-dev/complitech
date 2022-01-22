package by.complitech.demo.util.jwt.api;

import java.util.Date;

public interface IJwtUtil<T> {

    String generateAccessToken(T t);

    String getUserId(String token);

    String getLogin(String token);

    Date getExpirationDate(String token);

    boolean validate(String token);

}
