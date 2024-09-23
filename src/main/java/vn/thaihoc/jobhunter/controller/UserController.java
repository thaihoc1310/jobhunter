package vn.thaihoc.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.domain.response.RestCreateUserDTO;
import vn.thaihoc.jobhunter.domain.response.RestUpdateUserDTO;
import vn.thaihoc.jobhunter.domain.response.RestUserDTO;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.service.UserService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;
import vn.thaihoc.jobhunter.util.error.EmailInvalidException;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // @GetMapping("/users/create")
    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<RestCreateUserDTO> createNewUser(@Valid @RequestBody User user) throws EmailInvalidException {
        if (this.userService.handleCheckUserExistByEmail(user.getEmail())) {
            throw new EmailInvalidException("Email " + user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToRestCreateUserDTO(newUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (this.userService.handleGetUserById(id) == null) {
            throw new IdInvalidException("User with id = " + id + " not found");
        }
        this.userService.handleDeleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/users")
    @ApiMessage("Fetch Users")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<User> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.userService.handleGetAllUsers(spec, pageable));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch user by id")
    public ResponseEntity<RestUserDTO> getUser(@PathVariable("id") long id) throws IdInvalidException {
        User user = this.userService.handleGetUserById(id);
        if (user == null) {
            throw new IdInvalidException("User with id = " + id + " not found");
        }
        return ResponseEntity.ok(this.userService.convertToRestUserDTO(user));
        // return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<RestUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User updateUser = this.userService.handleUpdateUser(user);
        if (updateUser == null) {
            throw new IdInvalidException("User with id = " + user.getId() + " not found");
        }
        return ResponseEntity.ok(this.userService.convertToRestUpdateUserDTO(updateUser));
        // return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }
}
