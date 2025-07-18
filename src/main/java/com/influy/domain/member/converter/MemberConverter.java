package com.influy.domain.member.converter;

import com.influy.domain.category.entity.Category;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;

import java.util.List;

public class MemberConverter {
    public static Member toMember(MemberRequestDTO.UserJoin requestDTO, MemberRole role, String kakaoNickname) {
        //각 롤에 따라 프로필 생성로직
        return Member.builder()
                .kakaoId(requestDTO.getKakaoId())
                .nickname(requestDTO.getUsername()) //기본값이 username과 동일
                .role(role)
                .username(requestDTO.getUsername())
                .kakaoNickname(kakaoNickname)
                .build();
    }
    public static MemberResponseDTO.MemberProfile toMemberDTO(Member member) {
        //각 롤에 따라 프로필 생성로직
        //회원가입 로직 구현 시 수정
        return MemberResponseDTO.MemberProfile.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg())
                .createdAt(member.getCreatedAt())
                .build();
    }

}
