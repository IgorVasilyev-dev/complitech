package by.complitech.demo.service.userService.api;

import by.complitech.demo.dto.UserLogView;
import by.complitech.demo.model.User;

import java.util.List;

public interface IUserLogService {

    void logIn (User user);

    void logOut(User user);

    List<UserLogView> getList();

    void clearList();

}
