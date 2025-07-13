package com.influy.domain.member.service;

import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SellerProfileService sellerProfileService;

    @Override
    public Member findByKakaoId(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId)
                .orElseThrow(()->new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(()->new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public Member joinUser(MemberRequestDTO.UserJoin requestBody) {
        Member newMember = MemberConverter.toMember(requestBody, MemberRole.USER);

        return memberRepository.save(newMember);
    }

    @Override
    @Transactional
    public Member joinSeller(MemberRequestDTO.SellerJoin requestBody) {

        Member member = joinUser(requestBody.getUserInfo());
        sellerProfileService.createSellerProfile(member,requestBody);

        return member;
    }

    @Override
    @Transactional
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    @Override
    @Transactional
    public Member updateMemeber(Member member, MemberRequestDTO.UpdateProfile request) {

        return member.updateProfile(request);
    }

    @Override
    @Transactional
    public Member updateUsername(Member member, MemberRequestDTO.UpdateUsername request) {
        return member.updateUsername(request.getUsername());
    }
}
