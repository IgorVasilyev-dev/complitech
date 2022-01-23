package by.complitech.demo.controller.rest;

import by.complitech.demo.dto.AuthUser;
import by.complitech.demo.dto.CreateUserRequest;
import by.complitech.demo.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
public class UserAuthRestServlet {

    private final UserService userService;

    public UserAuthRestServlet(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/singIn", consumes = {"application/json"})
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

    @GetMapping(value = "/async", produces = {"application/json"})
    public DeferredResult<ResponseEntity<?>> handleReqDefResult(HttpServletRequest request,
                                                                @RequestParam(required = false) Long lastIndex) {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>(30000L);
        CompletableFuture.supplyAsync(() -> {
                    output.onTimeout(() ->
                            output.setErrorResult(
                                    ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                                            .body("Request timeout occurred.")));
                    while (userService.getUserLogList().size() <= lastIndex && !output.isSetOrExpired()) {
                        Thread.onSpinWait();
                    }
                    return userService.getUserLogList().stream().skip(lastIndex).collect(Collectors.toList());
                })
                .whenCompleteAsync((result, exc) -> output.setResult(ResponseEntity.ok(result)));
        return output;
    }

}
