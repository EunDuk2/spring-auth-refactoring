package com.example.oauth.common.auth.Service;

import com.example.oauth.member.domain.Member;
import com.example.oauth.member.domain.SocialType;
import com.example.oauth.member.dto.*;
import com.example.oauth.member.repository.MemberRepository;
import com.example.oauth.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoAuthService implements AuthService {
    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;
    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    public KakaoAuthService(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @Override
    public Member login(Object loginReqDto) {
        RedirectDto dto = (RedirectDto) loginReqDto;
        
        // accessToken 발급
        AccessTokenDto accessTokenDto = getAccessToken(dto.getCode());
        // 사용자 정보 얻기
        KakaoProfileDto kakaoProfileDto = getKakaoProfile(accessTokenDto.getAccess_token());

        Member originalMember = memberRepository.findBySocialId(kakaoProfileDto.getId()).orElse(null);

        if(originalMember == null) {
            MemberOauthCreateReqDto memberOauthCreateReqDto = MemberOauthCreateReqDto.builder()
                    .email(kakaoProfileDto.getKakao_account().getEmail())
                    .socialId(kakaoProfileDto.getId())
                    .socialType(SocialType.KAKAO)
                    .build();
            originalMember = memberService.createOauth(memberOauthCreateReqDto);
        }

        return originalMember;
    }

    public AccessTokenDto getAccessToken(String code) {
        RestClient restClient = RestClient.create();

        // MultiValueMap을 통해 자동으로 form-data 형식으로 body 조립 가능
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("grant_type", "authorization_code");

        ResponseEntity<AccessTokenDto> response = restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded")

                .body(params)

                .retrieve()
                .toEntity(AccessTokenDto.class);

        System.out.println("응답 AccessToken json" + response.getBody());

        return response.getBody();
    }

    public KakaoProfileDto getKakaoProfile(String token) {
        RestClient restClient = RestClient.create();

        ResponseEntity<KakaoProfileDto> response = restClient.post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer "+token)
                .retrieve()
                .toEntity(KakaoProfileDto.class);

        System.out.println("profile json" + response.getBody());

        return response.getBody();
    }
}
