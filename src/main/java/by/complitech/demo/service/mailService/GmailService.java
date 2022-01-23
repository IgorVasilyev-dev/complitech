package by.complitech.demo.service.mailService;

import by.complitech.demo.model.User;
import by.complitech.demo.service.mailService.api.IMailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.ValidationException;

public class GmailService implements IMailService {

    private final JavaMailSender emailSender;

    public GmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendRegistrationMail(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String mail = user.getEmail();
        try {
            InternetAddress internetAddress = new InternetAddress(mail);
            internetAddress.validate();
            simpleMailMessage.setTo(mail);
            simpleMailMessage.setSubject("Registration information");
            simpleMailMessage.setText("You have registered, your password: " + user.getPassword());
            emailSender.send(simpleMailMessage);
        } catch (AddressException e) {
            throw new ValidationException("Incorrect email: " + mail);
        }
    }
}
