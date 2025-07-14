package com.influy.domain.member.service;

import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
    Member joinUser(MemberRequestDTO.UserJoin requestBody);

    Member joinSeller(MemberRequestDTO.SellerJoin requestBody);

    Member findByKakaoId(Long kakaoId);

    Member findById(Long id);

    void deleteMember(Member member);

    Boolean checkUsername(String username);
}
