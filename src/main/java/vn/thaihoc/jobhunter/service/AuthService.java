package vn.thaihoc.jobhunter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.domain.dto.RestLoginDTO;
import vn.thaihoc.jobhunter.util.SecurityUtil;

@Service
public class AuthService {
    final private SecurityUtil sercurityUtil;
    final private UserService userService;

    @Value("${thaihoc.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthService(UserService userService, SecurityUtil sercurityUtil) {
        this.userService = userService;
        this.sercurityUtil = sercurityUtil;
    }

    public ResponseEntity<RestLoginDTO> createTokenAndCookie(String emailLogin) {
        User currentUser = this.userService.handleGetUserByUsername(emailLogin);
        RestLoginDTO res = new RestLoginDTO();
        res.setUser(new RestLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), currentUser.getName()));
        String access_token = this.sercurityUtil.createAccessToken(emailLogin, res.getUser());
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
}
