package com.example.oauth.common.auth.Service;

import com.example.oauth.member.domain.Member;

public interface AuthService {
    Member login(Object loginReqDto);
}
