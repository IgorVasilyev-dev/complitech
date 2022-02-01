package by.complitech.demo.service.userService;

import by.complitech.demo.dto.*;
import by.complitech.demo.model.RefreshToken;
import by.complitech.demo.model.User;
import by.complitech.demo.service.mailService.api.IMailService;
import by.complitech.demo.service.userService.api.IUserLogService;
import by.complitech.demo.service.userService.api.IUserService;
import by.complitech.demo.service.userService.api.IUserTokenService;
import by.complitech.demo.storage.api.IUserRepository;
import by.complitech.demo.util.passwordGenerate.api.IPasswordGenerateUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.List;

import static java.lang.String.format;

public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IMailService mailService;
    private final IPasswordGenerateUtil passwordGenerateUtil;
    private final IUserLogService userLoginNotificationService;
    private final IUserTokenService userTokenService;

    public UserService(IUserRepository userRepository, IMailService mailService,
                       IPasswordGenerateUtil passwordGenerateUtil,
                       IUserLogService userLoginNotificationService, IUserTokenService userTokenService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.passwordGenerateUtil = passwordGenerateUtil;
        this.userLoginNotificationService = userLoginNotificationService;
        this.userTokenService = userTokenService;
    }

    @Override
    @Transactional
    public void create(CreateUserRequest request) {
        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new ValidationException("Username exists!");
        }

        User user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordGenerateUtil.generatePassword());
        mailService.sendRegistrationMail(userRepository.save(user));
    }

    @Override
    public TokenRefreshResponse logIn(AuthUser request) {
        User user = userRepository.findByLogin(request.getLogin()).orElseThrow(
                () -> new ValidationException("invalid password or login")
        );
        if(!user.getPassword().equals(request.getPassword())) {
            throw new ValidationException("invalid password or login");
        }
        RefreshToken refreshToken = userTokenService.createRefreshToken(user);
        userLoginNotificationService.logIn(user);
        return new TokenRefreshResponse(userTokenService.generateAccessToken(user), refreshToken.getToken());
    }

    @Override
    public void logOut(String jwtToken) {
        if(!userTokenService.validate(jwtToken)) {
            throw new ValidationException("invalid jwtToken");
        }
        Long userId = Long.valueOf(userTokenService.getUserId(jwtToken));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(format("Entity with id = %d Not Found", userId)));
        userTokenService.deleteRefreshTokenByUserId(user);
        userLoginNotificationService.logOut(user);
    }

    @Override
    public TokenRefreshResponse refreshJwtToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return userTokenService.findByToken(requestRefreshToken)
                .map(userTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = userTokenService.generateAccessToken(user);
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @Override
    public List<UserLogView> getUserLogList() {
        return this.userLoginNotificationService.getList();
    }

    @Override
    public void clearUserLogList() {
        this.userLoginNotificationService.clearList();
    }


}
