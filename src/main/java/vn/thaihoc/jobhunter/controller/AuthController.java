package vn.thaihoc.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.domain.request.ReqLoginDTO;
import vn.thaihoc.jobhunter.domain.response.RestLoginDTO;
import vn.thaihoc.jobhunter.service.AuthService;
import vn.thaihoc.jobhunter.service.UserService;
import vn.thaihoc.jobhunter.util.SecurityUtil;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;
import vn.thaihoc.jobhunter.util.error.EmailInvalidException;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/v1")
public class AuthController {

    @Value("${thaihoc.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    final private AuthenticationManagerBuilder authenticationManagerBuilder;
    final private SecurityUtil sercurityUtil;
    final private UserService userService;
    final private AuthService authService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil sercurityUtil,
            UserService userService, AuthService authService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.sercurityUtil = sercurityUtil;
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    @ApiMessage("Login successfully")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO)
            throws MethodArgumentNotValidException {
        String emailLogin = loginDTO.getUsername();
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                emailLogin, loginDTO.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = this.authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // set thông tin người dùng vào SecurityContext, có thể sử dụng sau này
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return this.authService.createTokenAndCookie(emailLogin);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Fetch account")
    public ResponseEntity<RestLoginDTO.UserAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = this.userService.handleGetUserByUsername(email);
        RestLoginDTO.UserAccount userAccount = new RestLoginDTO.UserAccount();
        userAccount.setUser(new RestLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(),
                currentUser.getName()));
        return ResponseEntity.ok(userAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<RestLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "nullval") String refreshToken)
            throws EmailInvalidException, IdInvalidException {
        if (refreshToken.equals("nullval")) {
            throw new IdInvalidException("Refresh token is invalid");
        }
        Jwt decondedToken = this.sercurityUtil.checkValidRefreshToken(refreshToken);
        String email = decondedToken.getSubject();
        // check user by token and email
        User user = this.userService.handleGetUserByRefreshTokenAndEmail(refreshToken, email);
        if (user == null) {
            throw new EmailInvalidException("Refresh token is invalid");
        }
        return this.authService.createTokenAndCookie(email);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout successfully")
    public ResponseEntity<Void> logout() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.userService.handleUpdateUserToken(email, null);
        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

}