package by.complitech.demo.service.mailService;

import by.complitech.demo.model.User;
import by.complitech.demo.service.mailService.api.IMailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class GmailService implements IMailService {

    private final JavaMailSender emailSender;

    public GmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendRegistrationMail(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Registration information");
        simpleMailMessage.setText("You have registered, your password: " + user.getPassword());
        emailSender.send(simpleMailMessage);
    }
}
