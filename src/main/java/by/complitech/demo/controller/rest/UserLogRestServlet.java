package by.complitech.demo.controller.rest;

import by.complitech.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/log")
public class UserLogRestServlet {

    private final UserService userService;

    public UserLogRestServlet(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/async", produces = {"application/json"})
    public DeferredResult<ResponseEntity<?>> handleReqDefResult(@RequestParam(required = false, defaultValue = "0") Long lastIndex) {
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
