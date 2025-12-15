package com.example.oauth.common.auth.Service;

import com.example.oauth.common.auth.dto.OauthLoginDto;
import com.example.oauth.member.domain.Member;

public interface OauthService {
    Member login(OauthLoginDto oauthLoginDto);
}

