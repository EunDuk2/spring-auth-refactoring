package com.example.oauth.common.auth.Service;

import com.example.oauth.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    Member login(Object loginReqDto);
}
