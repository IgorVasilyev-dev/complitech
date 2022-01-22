package by.complitech.demo.util.passwordGenerate;

import by.complitech.demo.util.passwordGenerate.api.IPasswordGenerateUtil;

import java.util.Random;
import java.util.stream.Collectors;

public class DefaultPasswordGenerateUtil implements IPasswordGenerateUtil {

    private final int lengthPassword;

    public DefaultPasswordGenerateUtil(int lengthPassword) {
        this.lengthPassword = lengthPassword;
    }

    public String generatePassword() {
        return new Random().ints(lengthPassword, 97, 122)
                .mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
    }

}
