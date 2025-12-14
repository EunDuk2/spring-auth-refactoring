package com.example.oauth.member.service;

import com.example.oauth.member.domain.Member;
import com.example.oauth.member.dto.MemberEmailCreateReqDto;
import com.example.oauth.member.dto.MemberOauthCreateReqDto;

public interface MemberService {
    Member create(MemberEmailCreateReqDto reqDto);
    Member createOauth(MemberOauthCreateReqDto reqDto);
}
