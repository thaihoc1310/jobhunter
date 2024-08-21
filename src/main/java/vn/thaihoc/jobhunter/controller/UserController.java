package vn.thaihoc.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.service.UserService;
import vn.thaihoc.jobhunter.service.error.IdInvalidException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("/users/create")
    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        User newUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id > 1000)
            throw new IdInvalidException("id greater than 1000");
        this.userService.handleDeleteUserById(id);
        // return ResponseEntity.ok("delete user with id : " + id);
        return ResponseEntity.status(HttpStatus.OK).body("delete user with id : " + id);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = this.userService.handleGetAllUsers();
        return ResponseEntity.ok(allUsers);
        // return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        User user = this.userService.handleGetUserById(id);
        return ResponseEntity.ok(user);
        // return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updateUser = this.userService.handleUpdateUser(user);
        return ResponseEntity.ok(updateUser);
        // return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }
}
