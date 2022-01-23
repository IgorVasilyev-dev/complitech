package by.complitech.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserLogView {

    private String status;

    private String fullName;

    private String login;

    private Date date;

}
