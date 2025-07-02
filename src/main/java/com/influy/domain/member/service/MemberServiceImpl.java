package com.influy.domain.member.service;

import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.domain.userProfile.service.UserProfileService;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final UserProfileService userProfileService;
    private final SellerProfileService sellerProfileService;

    @Transactional
    @Override
    public Member joinUser(MemberRequestDTO.UserJoin requestBody) {
        Member newMember = MemberConverter.toMember(requestBody);
        Member member = memberRepository.save(newMember);

        userProfileService.createUserProfile(member);

        return member;
    }

    @Override
    @Transactional
    public Member joinSeller(MemberRequestDTO.SellerJoin requestBody) {

        Member newMember = MemberConverter.toMember(requestBody.getUserInfo());
        Member member = memberRepository.save(newMember);

        userProfileService.createUserProfile(member);
        sellerProfileService.createSellerProfile(member,requestBody);
        return member;
    }

    @Override
    public Member findByKakaoId(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId)
                .orElseThrow(()->new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
