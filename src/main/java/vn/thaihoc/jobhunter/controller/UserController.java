package vn.thaihoc.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Null;
import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.service.UserService;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("/user/create")
    @PostMapping("/user")
    public User createNewUser(@RequestBody User user) {
        return this.userService.handleCreateUser(user);
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUserById(id);
        return "Delete user";
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return this.userService.handleGetAllUsers();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") long id) {
        return this.userService.handleGetUserById(id);
    }
}
