package by.complitech.demo.config;

import by.complitech.demo.service.UserLogService;
import by.complitech.demo.service.UserService;
import by.complitech.demo.service.mailService.GmailService;
import by.complitech.demo.service.mailService.api.IMailService;
import by.complitech.demo.storage.api.IUserRepository;
import by.complitech.demo.util.jwt.JwtUtil;
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
    UserService userService(IUserRepository repository, JwtUtil jwtUtil, IMailService mailService,
                            IPasswordGenerateUtil passwordGenerateUtil, UserLogService userLoginNotificationService) {
        return new UserService(repository, jwtUtil, mailService, passwordGenerateUtil, userLoginNotificationService);
    }

    @Bean
    UserLogService userLoginNotificationService() {
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

}
