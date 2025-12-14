package com.example.oauth.member.service;

import com.example.oauth.member.domain.Member;
import com.example.oauth.member.dto.MemberEmailCreateReqDto;
import com.example.oauth.member.dto.MemberOauthCreateReqDto;
import com.example.oauth.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member create(MemberEmailCreateReqDto reqDto) {
        Member member = Member.builder()
                .email(reqDto.getEmail())
                .password(passwordEncoder.encode(reqDto.getPassword()))
                .build();
        memberRepository.save(member);
        return member;
    }

    @Override
    public Member createOauth(MemberOauthCreateReqDto reqDto) {
        Member member = Member.builder()
                .email(reqDto.getEmail())
                .socialType(reqDto.getSocialType())
                .socialId(reqDto.getSocialId())
                .build();
        memberRepository.save(member);
        return member;
    }
}
