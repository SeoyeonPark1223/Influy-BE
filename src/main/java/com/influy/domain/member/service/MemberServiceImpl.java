package com.influy.domain.member.service;

import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.sellerProfile.converter.SellerProfileConverter;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.userProfile.entity.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Member join(MemberRequestDTO.Join requestBody) {
        Member member = MemberConverter.toMember(requestBody);
       // UserProfile userProfile = userProfileService.createUserProfile()
        return memberRepository.save(member);
    }
}
