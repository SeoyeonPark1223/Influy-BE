package com.influy.domain.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.auth.dto.AuthRequestDTO;
import com.influy.global.auth.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SellerProfileService sellerProfileService;
    private final SellerProfileRepository sellerProfileRepository;
    private final AuthService authService;

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
    public Member joinUser(MemberRequestDTO.UserJoin requestBody,MemberRole role) {


        AuthRequestDTO.KakaoUserProfile profile = authService.getUserProfile(requestBody.getKakaoId());


        String kakaoNickname = profile.getKakao_account().getProfile().getNickname();
        Member newMember = MemberConverter.toMember(requestBody, role, kakaoNickname);
        return memberRepository.save(newMember);

    }

    @Override
    @Transactional
    public Member joinSeller(MemberRequestDTO.SellerJoin requestBody) {

        if(sellerProfileRepository.existsByEmail(requestBody.getEmail())){
            throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }
        if(sellerProfileRepository.existsByInstagram(requestBody.getInstagram())){
            throw new GeneralException(ErrorStatus.INSTAGRAM_ALREADY_EXISTS);
        }

        Member member = joinUser(requestBody.getUserInfo(),MemberRole.SELLER);
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

    @Override
    public Boolean checkUsername(String username) {
        return memberRepository.existsByUsername(username);
    }
}
