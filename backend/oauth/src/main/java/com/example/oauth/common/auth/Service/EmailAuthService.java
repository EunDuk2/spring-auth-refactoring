package com.example.oauth.common.auth.Service;

import com.example.oauth.common.auth.dto.EmailLoginReqDto;
import com.example.oauth.member.domain.Member;
import com.example.oauth.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmailAuthService implements AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmailAuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Member login(Object loginReqDto) {
        EmailLoginReqDto reqDto = (EmailLoginReqDto) loginReqDto;

        Member member = memberRepository.findByEmail(reqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));

        if(!passwordEncoder.matches(reqDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("password가 일치하지 않습니다.");
        }
        return member;
    }
}
