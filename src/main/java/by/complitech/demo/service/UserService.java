package by.complitech.demo.service;

import by.complitech.demo.dto.AuthUser;
import by.complitech.demo.dto.CreateUserRequest;
import by.complitech.demo.dto.JwtToken;
import by.complitech.demo.dto.UserLogView;
import by.complitech.demo.model.User;
import by.complitech.demo.service.mailService.api.IMailService;
import by.complitech.demo.storage.api.IUserRepository;
import by.complitech.demo.util.jwt.JwtUtil;
import by.complitech.demo.util.passwordGenerate.api.IPasswordGenerateUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;

public class UserService {

    private final IUserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final IMailService mailService;
    private final IPasswordGenerateUtil passwordGenerateUtil;
    private final UserLogService userLoginNotificationService;

    public UserService(IUserRepository userRepository, JwtUtil jwtUtil, IMailService mailService,
                       IPasswordGenerateUtil passwordGenerateUtil, UserLogService userLoginNotificationService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.mailService = mailService;
        this.passwordGenerateUtil = passwordGenerateUtil;
        this.userLoginNotificationService = userLoginNotificationService;
    }

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

    public JwtToken logIn(AuthUser request) {
        User user = userRepository.findByLogin(request.getLogin()).orElseThrow(
                () -> new ValidationException("invalid password or login")
        );
        if(!user.getPassword().equals(request.getPassword())) {
            throw new ValidationException("invalid password or login");
        }
        userLoginNotificationService.logIn(user);
        return new JwtToken(jwtUtil.generateAccessToken(user));
    }

    public void logOut(String jwtToken) {
        if(jwtUtil.validate(jwtToken)) {
            Long userId = Long.valueOf(jwtUtil.getUserId(jwtToken));
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new ValidationException("invalid jwtToken"));
            userLoginNotificationService.logOut(user);
        }
    }

    public List<UserLogView> getUserLogList() {
        return this.userLoginNotificationService.getList();
    }

    public void clearUserLogList() {
        this.userLoginNotificationService.clearList();
    }


}
