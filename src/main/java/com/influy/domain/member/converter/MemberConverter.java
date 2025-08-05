package com.influy.domain.member.converter;

import com.influy.domain.category.entity.Category;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.sellerProfile.dto.SellerProfileResponseDTO;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;

import java.util.List;
import java.util.Objects;

public class MemberConverter {
    public static Member toMember(MemberRequestDTO.UserJoin requestDTO, MemberRole role, String kakaoNickname) {

        String username = requestDTO.getUsername();
        String nickname = requestDTO.getUsername();//초기값이 username과 동일, 8자보다 길면 8자에서 끊김
        if(nickname.length()>8){
            nickname = nickname.substring(0,8);
        }

        //각 롤에 따라 프로필 생성로직
        return Member.builder()
                .kakaoId(requestDTO.getKakaoId())
                .nickname(nickname)
                .role(role)
                .username(username)
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

    public static MemberResponseDTO.Setting toSettingDTO(Member member) {
        if(member.getRole()==MemberRole.SELLER||member.getRole()==MemberRole.ADMIN){
            return SellerProfileResponseDTO.SellerSetting.builder()
                    .username(member.getUsername())
                    .isPublic(Objects.requireNonNull(member.getSellerProfile(), "셀러 프로필이 없습니다.").getIsPublic())
                    .build();
        }else{
            return MemberResponseDTO.MemberSetting.builder()
                    .username(member.getUsername())
                    .build();
        }
    }
}
