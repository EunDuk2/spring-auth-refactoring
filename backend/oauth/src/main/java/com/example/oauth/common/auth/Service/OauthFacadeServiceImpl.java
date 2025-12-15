package com.example.oauth.common.auth.Service;

import com.example.oauth.common.auth.dto.OauthLoginDto;
import com.example.oauth.member.domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
public class OauthFacadeServiceImpl implements OauthFacadeService {
    private final Map<String, OauthService> oauthServiceStrategy;

    @Autowired
    public OauthFacadeServiceImpl(Map<String, OauthService> oauthServiceStrategy) {
        this.oauthServiceStrategy = oauthServiceStrategy;
    }

    @Override
    public Member login(String type, OauthLoginDto oauthLoginDto) {
        // type 으로 전략 선택
        OauthService oauthService = oauthServiceStrategy.get(type);
        return oauthService.login(oauthLoginDto);
    }
}
