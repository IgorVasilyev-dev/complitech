package by.complitech.demo.service.userService.api;

import by.complitech.demo.dto.*;

import java.util.List;

public interface IUserService {

    void create(CreateUserRequest request);

    TokenRefreshResponse logIn(AuthUser request);

    void logOut(String jwtToken);

    TokenRefreshResponse refreshJwtToken(TokenRefreshRequest request);

    List<UserLogView> getUserLogList();

    void clearUserLogList();
}
