package by.complitech.demo.service;

import by.complitech.demo.dto.UserLogView;
import by.complitech.demo.model.User;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserLogService {

    private final List<UserLogView> userLoginNotificationList = new CopyOnWriteArrayList<>();

    public void logIn (User user) {
        UserLogView userView = new UserLogView("logIn",user.getFullName(), user.getLogin(), new Date());
        userLoginNotificationList.add(userView);
    }

    public void logOut(User user) {
        UserLogView userView = new UserLogView("logOut",user.getFullName(), user.getLogin(), new Date());
        userLoginNotificationList.add(userView);
    }

    public List<UserLogView> getList() {
        return this.userLoginNotificationList;
    }

    public void clearList() {
        this.userLoginNotificationList.clear();
    }

}
