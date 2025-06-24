package com.influy.domain.member.service;

import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;

public interface MemberService {
    Member joinUser(MemberRequestDTO.UserJoin requestBody);

    Member joinSeller(MemberRequestDTO.SellerJoin requestBody);
}
