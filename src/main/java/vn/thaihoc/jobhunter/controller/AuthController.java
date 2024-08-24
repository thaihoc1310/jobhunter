package vn.thaihoc.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.thaihoc.jobhunter.domain.dto.LoginDTO;
import vn.thaihoc.jobhunter.domain.dto.RestLoginDTO;
import vn.thaihoc.jobhunter.util.SercurityUtil;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthController {
    final private AuthenticationManagerBuilder authenticationManagerBuilder;
    final private SercurityUtil sercurityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SercurityUtil sercurityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.sercurityUtil = sercurityUtil;
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
        RestLoginDTO res = new RestLoginDTO(access_token);
        return ResponseEntity.ok().body(res);
    }
}
