package com.example.oauth.common.auth.Service;

import com.example.oauth.member.domain.Member;
import com.example.oauth.common.auth.dto.MemberLoginDto;

public interface AuthService {
    Member login(MemberLoginDto memberLoginDto);
}
