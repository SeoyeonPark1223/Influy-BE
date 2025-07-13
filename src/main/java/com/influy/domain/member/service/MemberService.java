package com.influy.domain.member.service;

import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    Member joinUser(MemberRequestDTO.UserJoin requestBody);

    Member joinSeller(MemberRequestDTO.SellerJoin requestBody);

    Member findByKakaoId(Long kakaoId);

    Member findById(Long id);

    void deleteMember(Member member);

    Member updateMemeber(Member member, MemberRequestDTO.UpdateProfile request);

    Member updateUsername(Member member, MemberRequestDTO.UpdateUsername request);
}
