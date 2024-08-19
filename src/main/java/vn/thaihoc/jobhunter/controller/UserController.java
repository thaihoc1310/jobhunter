package vn.thaihoc.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/create")
    public String createNewUser() {
        User user = new User();
        user.setName("Hoc");
        user.setEmail("thaihoc131005@gmail.com");
        user.setPassword("123456");
        this.userService.handleCreateUser(user);
        return "User created";
    }
}
