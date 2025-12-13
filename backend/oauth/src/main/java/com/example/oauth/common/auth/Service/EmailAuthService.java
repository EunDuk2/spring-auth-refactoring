package com.example.oauth.common.auth.Service;

import com.example.oauth.common.auth.dto.EmailLoginReqDto;
import com.example.oauth.member.domain.Member;
import com.example.oauth.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        Optional<Member> optMember = memberRepository.findByEmail(reqDto.getEmail());
        if(!optMember.isPresent()) {
            throw new IllegalArgumentException("email이 존재하지 않습니다.");
        }
        Member member= optMember.get();

        if(!passwordEncoder.matches(reqDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("password가 일치하지 않습니다.");
        }
        return member;
    }
}
