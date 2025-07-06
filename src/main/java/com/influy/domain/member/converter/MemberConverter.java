package com.influy.domain.member.converter;

import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;

public class MemberConverter {
    public static Member toMember(MemberRequestDTO.UserJoin requestDTO, MemberRole role) {
        //각 롤에 따라 프로필 생성로직
        //회원가입 로직 구현 시 수정
        return Member.builder()
                .kakaoId(requestDTO.getKakaoId())
                .nickname(requestDTO.getNickname())
                .name(requestDTO.getName())
                .role(role)
                .username(requestDTO.getUsername())
                .build();
    }
    public static MemberResponseDTO.MemberProfile toMemberDTO(Member member) {
        //각 롤에 따라 프로필 생성로직
        //회원가입 로직 구현 시 수정
        return MemberResponseDTO.MemberProfile.builder()
                .profileImg(member.getProfileImg())
                .name(member.getName())
                .id(member.getId())
                .username(member.getUsername())
                .createdAt(member.getCreatedAt())
                .build();
    }

}
