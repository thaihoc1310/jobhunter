package vn.thaihoc.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/v1")
public class AuthController {

    @Value("${thaihoc.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    final private AuthenticationManagerBuilder authenticationManagerBuilder;
    final private SecurityUtil sercurityUtil;
    final private UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil sercurityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.sercurityUtil = sercurityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    @ApiMessage("Login successfully")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO)
            throws MethodArgumentNotValidException {
        String emailLogin = loginDTO.getUsername();

        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                emailLogin, loginDTO.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = this.authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User currentUser = this.userService.handleGetUserByUsername(emailLogin);
        RestLoginDTO res = new RestLoginDTO();
        res.setUser(new RestLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getName()));
        String access_token = this.sercurityUtil.createAccessToken(authentication, res.getUser());
        res.setAccessToken(access_token);
        // create refresh token
        String refresh_token = this.sercurityUtil.createRefreshToken(emailLogin, res);
        this.userService.handleUpdateUserToken(emailLogin, refresh_token);

        // set cookie
        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Fetch account")
    public ResponseEntity<RestLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUser = this.userService.handleGetUserByUsername(email);
        RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(),
                currentUser.getName());
        return ResponseEntity.ok(userLogin);
    }

}
