package by.complitech.demo.service.userService;

import by.complitech.demo.dto.UserLogView;
import by.complitech.demo.model.User;
import by.complitech.demo.service.userService.api.IUserLogService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserLogService implements IUserLogService {

    private final List<UserLogView> userLoginNotificationList = new CopyOnWriteArrayList<>();

    @Override
    public void logIn (User user) {
        UserLogView userView = new UserLogView("logIn",user.getFullName(), user.getLogin(), new Date());
        userLoginNotificationList.add(userView);
    }

    @Override
    public void logOut(User user) {
        UserLogView userView = new UserLogView("logOut",user.getFullName(), user.getLogin(), new Date());
        userLoginNotificationList.add(userView);
    }

    @Override
    public List<UserLogView> getList() {
        return this.userLoginNotificationList;
    }

    @Override
    public void clearList() {
        this.userLoginNotificationList.clear();
    }

}
