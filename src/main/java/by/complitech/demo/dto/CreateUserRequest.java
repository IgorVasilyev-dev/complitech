package by.complitech.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class CreateUserRequest {

    @NotBlank
    private String login;
    @NotBlank
    private String email;
    @NotBlank
    private String fullName;

}
