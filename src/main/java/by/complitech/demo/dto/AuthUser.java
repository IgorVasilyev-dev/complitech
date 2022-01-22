package by.complitech.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthUser {

    @NotNull
    private String login;

    @NotNull
    private String password;

}
