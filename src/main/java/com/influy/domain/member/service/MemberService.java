package com.influy.domain.member.service;

import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.jwt.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    Member joinUser(MemberRequestDTO.UserJoin requestBody, MemberRole role);

    Member joinSeller(MemberRequestDTO.SellerJoin requestBody);

    Member findByKakaoId(Long kakaoId);

    Member findById(Long id);

    void deleteMember(Member member);

    Member updateMemeber(Member member, MemberRequestDTO.UpdateProfile request);

    Member updateUsername(Member member, MemberRequestDTO.UpdateUsername request);

    Boolean checkUsername(String username);

    SellerProfile checkSeller(CustomUserDetails userDetails);
}
