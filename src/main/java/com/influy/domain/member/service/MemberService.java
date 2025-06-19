package com.influy.domain.member.service;

import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;

public interface MemberService {
    Member join(MemberRequestDTO.Join requestBody);
}
