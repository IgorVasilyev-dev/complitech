package by.complitech.demo.config;

import by.complitech.demo.model.User;
import by.complitech.demo.service.userService.UserTokenService;
import by.complitech.demo.service.userService.UserLogService;
import by.complitech.demo.service.userService.UserService;
import by.complitech.demo.service.mailService.GmailService;
import by.complitech.demo.service.mailService.api.IMailService;
import by.complitech.demo.service.userService.api.IUserLogService;
import by.complitech.demo.service.userService.api.IUserService;
import by.complitech.demo.service.userService.api.IUserTokenService;
import by.complitech.demo.storage.api.IRefreshTokenRepository;
import by.complitech.demo.storage.api.IUserRepository;
import by.complitech.demo.util.jwt.api.IJwtUtil;
import by.complitech.demo.util.passwordGenerate.DefaultPasswordGenerateUtil;
import by.complitech.demo.util.passwordGenerate.api.IPasswordGenerateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@ComponentScan("by.complitech.demo.config")
public class AppConfiguration {

    @Bean
    IUserService userService(IUserRepository repository, IMailService mailService,
                             IPasswordGenerateUtil passwordGenerateUtil,
                             IUserLogService userLoginNotificationService,
                             IUserTokenService userTokenService) {
        return new UserService(repository, mailService, passwordGenerateUtil, userLoginNotificationService, userTokenService);
    }

    @Bean
    IUserLogService userLoginNotificationService() {
        return new UserLogService();
    }

    @Bean
    IMailService mailService(JavaMailSender javaMailSender) {
        return new GmailService(javaMailSender);
    }

    @Bean
    IPasswordGenerateUtil passwordGenerateUtil() {
        return new DefaultPasswordGenerateUtil(16);
    }

    @Bean
    IUserTokenService refreshTokenService(IJwtUtil<User> jwtUtil, IRefreshTokenRepository refreshTokenRepository) {
        return new UserTokenService(jwtUtil,refreshTokenRepository);
    }

}
