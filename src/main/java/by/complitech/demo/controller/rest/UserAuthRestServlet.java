package by.complitech.demo.controller.rest;

import by.complitech.demo.dto.AuthUser;
import by.complitech.demo.dto.CreateUserRequest;
import by.complitech.demo.dto.TokenRefreshRequest;
import by.complitech.demo.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user/auth")
public class UserAuthRestServlet {

    private final UserService userService;

    public UserAuthRestServlet(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/singIn", consumes = {"application/json"}, produces = {"application/json"} )
    protected ResponseEntity<?> signInUser(@Valid @RequestBody AuthUser request) {
        return ResponseEntity.ok().body(userService.logIn(request));
    }

    @PostMapping(value = "/singUp", consumes = {"application/json"}, produces = {"application/json"})
    protected ResponseEntity<?> signUpUser(@Valid @RequestBody CreateUserRequest request) {
        userService.create(request);
        return ResponseEntity.ok().body("Successfully registered");
    }

    @GetMapping(value = "/logout", produces = {"application/json"})
    protected ResponseEntity<?> logOutUser(HttpServletRequest request) {
        userService.logOut(request.getHeader(HttpHeaders.AUTHORIZATION));
        return ResponseEntity.ok().body("logOut is Success");
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<?> refreshJwtToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok().body(userService.refreshJwtToken(request));
    }

}
