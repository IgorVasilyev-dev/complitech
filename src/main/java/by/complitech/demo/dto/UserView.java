package by.complitech.demo.dto;

import lombok.Data;

@Data
public class UserView {

    private Long id;

    private String fullName;

    private String login;

    private String email;

    private String password;
}
