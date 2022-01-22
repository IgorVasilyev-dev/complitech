package by.complitech.demo.service;

import by.complitech.demo.dto.AuthUser;
import by.complitech.demo.dto.CreateUserRequest;
import by.complitech.demo.dto.JwtToken;
import by.complitech.demo.model.User;
import by.complitech.demo.service.mailService.api.IMailService;
import by.complitech.demo.storage.api.IUserRepository;
import by.complitech.demo.util.jwt.JwtUtil;
import by.complitech.demo.util.passwordGenerate.api.IPasswordGenerateUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;

public class UserService {

    private final IUserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final IMailService mailService;
    private final IPasswordGenerateUtil passwordGenerateUtil;

    public UserService(IUserRepository userRepository, JwtUtil jwtUtil, IMailService mailService, IPasswordGenerateUtil passwordGenerateUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.mailService = mailService;
        this.passwordGenerateUtil = passwordGenerateUtil;
    }

    @Transactional
    public User create(CreateUserRequest request) {
        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new ValidationException("Username exists!");
        }

        User user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordGenerateUtil.generatePassword());
        mailService.sendRegistrationMail(userRepository.save(user));
        return user;
    }

    public JwtToken logIn(AuthUser request) {
        User user = userRepository.findByLogin(request.getLogin()).orElseThrow(
                () -> new ValidationException("invalid password or login")
        );
        if(!user.getPassword().equals(request.getPassword())) {
            throw new ValidationException("invalid password or login");
        }
        System.out.println("logIn");
        return new JwtToken(jwtUtil.generateAccessToken(user));
    }

    public void logOut(String jwtToken) {
        if(jwtUtil.validate(jwtToken)) {
            Long userId = Long.valueOf(jwtUtil.getUserId(jwtToken));
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new ValidationException("invalid jwtToken"));
        }
    }

}
