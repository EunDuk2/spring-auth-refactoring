package com.example.oauth.member.dto;

import com.example.oauth.member.domain.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberOauthCreateReqDto {
    private String email;
    private String socialId;
    private SocialType socialType;
}
