package vn.thaihoc.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.domain.dto.LoginDTO;
import vn.thaihoc.jobhunter.domain.dto.RestLoginDTO;
import vn.thaihoc.jobhunter.service.UserService;
import vn.thaihoc.jobhunter.util.SecurityUtil;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api/v1")
public class AuthController {
    final private AuthenticationManagerBuilder authenticationManagerBuilder;
    final private SecurityUtil sercurityUtil;
    final private UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil sercurityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.sercurityUtil = sercurityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO)
            throws MethodArgumentNotValidException {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = this.authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // create token
        String access_token = this.sercurityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User currentUser = this.userService.handleGetUserByUsername(loginDTO.getUsername());
        RestLoginDTO res = new RestLoginDTO();
        res.setAccessToken(access_token);
        res.setUser(new RestLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getName()));
        return ResponseEntity.ok().body(res);
    }
}
