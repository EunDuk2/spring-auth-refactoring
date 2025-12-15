package com.example.oauth.common.auth.Service;

import com.example.oauth.member.domain.Member;
import com.example.oauth.member.domain.SocialType;
import com.example.oauth.member.dto.AccessTokenDto;
import com.example.oauth.common.auth.dto.GoogleProfileDto;
import com.example.oauth.member.dto.MemberOauthCreateReqDto;
import com.example.oauth.common.auth.dto.OauthLoginDto;
import com.example.oauth.member.repository.MemberRepository;
import com.example.oauth.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service("google")
public class GoogleAuthService implements OauthService {
    @Value("${oauth.google.client-id}")
    private String googleClientId;
    @Value("${oauth.google.client-secret}")
    private String googleClientSecret;
    @Value("${oauth.google.redirect-uri}")
    private String googleRedirectUri;

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    public GoogleAuthService(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @Override
    public Member login(OauthLoginDto oauthLoginDto) {
        // accessToken 발급
        AccessTokenDto accessTokenDto = getAccessToken(oauthLoginDto.getCode());
        // 사용자 정보 얻기
        GoogleProfileDto googleProfileDto = getGoogleProfile(accessTokenDto.getAccess_token());
        // 사용자 없으면 회원가입까지 수행
        Member originalMember = memberRepository.findBySocialId(googleProfileDto.getSub()).orElse(null);
        if(originalMember == null) {
            MemberOauthCreateReqDto memberOauthCreateReqDto = MemberOauthCreateReqDto.builder()
                    .email(googleProfileDto.getEmail())
                    .socialId(googleProfileDto.getSub())
                    .socialType(SocialType.GOOGLE)
                    .build();
            originalMember = memberService.createOauth(memberOauthCreateReqDto);
        }

        return originalMember;
    }

    public AccessTokenDto getAccessToken(String code) {
        // 인가코드, clientId, client_secret redirect_uri, grant_type

        // Spring6부터 RestTemplate 비추천상태이기에, 대신 RestClient 사용
        RestClient restClient = RestClient.create();

        // MultiValueMap을 통해 자동으로 form-data 형식으로 body 조립 가능
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        ResponseEntity<AccessTokenDto> response = restClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                // ?code=xxx&client_id=yyyy& ...
                .body(params)
                // retrieve는 응답 body값만을 추출한다.
                .retrieve()
                .toEntity(AccessTokenDto.class);

        System.out.println("응답 AccessToken json" + response.getBody());

        return response.getBody();
    }

    public GoogleProfileDto getGoogleProfile(String token) {
        RestClient restClient = RestClient.create();

        ResponseEntity<GoogleProfileDto> response = restClient.post()
                .uri("https://openidconnect.googleapis.com/v1/userinfo")
                .header("Authorization", "Bearer "+token)
                .retrieve()
                .toEntity(GoogleProfileDto.class);

        System.out.println("profile json" + response.getBody());

        return response.getBody();
    }
}
