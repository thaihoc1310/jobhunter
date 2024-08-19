package vn.thaihoc.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("/user/create")
    @PostMapping("/user/create")
    public User createNewUser(@RequestBody User user) {
        return this.userService.handleCreateUser(user);
    }
}
