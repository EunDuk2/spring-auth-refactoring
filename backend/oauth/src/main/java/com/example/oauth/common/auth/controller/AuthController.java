package com.example.oauth.common.auth.controller;

import com.example.oauth.common.auth.Service.AuthService;
import com.example.oauth.common.auth.dto.EmailLoginReqDto;
import com.example.oauth.common.auth.original.JwtTokenProvider;
import com.example.oauth.member.domain.Member;
import com.example.oauth.member.dto.RedirectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // 로그인 서비스 의존성 주입 받고
    private final Map<String, AuthService> authServiceStrategy;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(Map<String, AuthService> authServiceStrategy, JwtTokenProvider jwtTokenProvider) {
        this.authServiceStrategy = authServiceStrategy;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/email")
    public ResponseEntity<?> emailLogin(@RequestBody EmailLoginReqDto dto) {
        // 0. type 결정
        AuthService authService = authServiceStrategy.get("emailAuthService");

        // 1. login 검증
        Member member = authService.login(dto);

        // 2. 토큰 발급
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

}
