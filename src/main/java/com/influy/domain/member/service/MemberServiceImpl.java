package com.influy.domain.member.service;

import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SellerProfileService sellerProfileService;

    @Transactional
    @Override
    public Member joinUser(MemberRequestDTO.UserJoin requestBody) {
        Member newMember = MemberConverter.toMember(requestBody);
        Member member = memberRepository.save(newMember);

        return member;
    }

    @Override
    @Transactional
    public Member joinSeller(MemberRequestDTO.SellerJoin requestBody) {

        Member newMember = MemberConverter.toMember(requestBody.getUserInfo());
        Member member = memberRepository.save(newMember);

        sellerProfileService.createSellerProfile(member,requestBody);
        return member;
    }
}
