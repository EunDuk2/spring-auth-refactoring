package com.example.oauth.common.auth.Service;

import com.example.oauth.member.domain.Member;
import com.example.oauth.common.auth.dto.OauthLoginDto;

public interface OauthFacadeService {
    Member login(String type, OauthLoginDto oauthLoginDto);
}

