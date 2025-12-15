package com.example.oauth.common.auth.controller;

import com.example.oauth.common.auth.Service.OauthFacadeService;
import com.example.oauth.common.auth.original.JwtTokenProvider;
import com.example.oauth.member.domain.Member;
import com.example.oauth.common.auth.dto.OauthLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    // 로그인 서비스 의존성 주입 받고
    private final OauthFacadeService oauthFacadeService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public OauthController(OauthFacadeService oauthFacadeService, JwtTokenProvider jwtTokenProvider) {
        this.oauthFacadeService = oauthFacadeService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody OauthLoginDto oauthLoginDto) {
        // 1. login 검증
        Member member = oauthFacadeService.login("google", oauthLoginDto);

        // 2. 토큰 발급
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody OauthLoginDto oauthLoginDto) {
        // 1. login 검증
        Member member = oauthFacadeService.login("kakao", oauthLoginDto);

        // 2. 토큰 발급
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

}
