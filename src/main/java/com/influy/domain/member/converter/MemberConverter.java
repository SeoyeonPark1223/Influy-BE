package com.influy.domain.member.converter;

import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;

public class MemberConverter {
    public static Member toMember(MemberRequestDTO.UserJoin requestDTO) {
        //각 롤에 따라 프로필 생성로직
        //회원가입 로직 구현 시 수정
        return Member.builder()
                .nickname(requestDTO.getNickname())
                .name(requestDTO.getName())
                .role(requestDTO.getRole())
                .password(requestDTO.getPassword()) //나중엔 hash값으로
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
