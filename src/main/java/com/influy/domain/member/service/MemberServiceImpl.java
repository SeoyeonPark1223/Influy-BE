package com.influy.domain.member.service;

import com.influy.domain.category.entity.Category;
import com.influy.domain.category.repository.CategoryRepository;
import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.memberCategory.converter.MemberCategoryConverter;
import com.influy.domain.memberCategory.entity.MemberCategory;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.auth.dto.AuthRequestDTO;
import com.influy.global.auth.service.AuthService;
import com.influy.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SellerProfileService sellerProfileService;
    private final SellerProfileRepository sellerProfileRepository;
    private final AuthService authService;
    private final CategoryRepository categoryRepository;

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

        List<Category> interestedItemCategories= new ArrayList<>();

        //관심 카테고리 설정
        if(requestBody.getInterestedCategories()==null){
            throw new GeneralException(ErrorStatus.REQUIRES_INTERESTED_CATEGORY);
        }
        List<Long> ids = requestBody.getInterestedCategories();
        interestedItemCategories = categoryRepository.findAllById(ids);
        //요청 들어온 카테고리 개수와 반환된 개수가 다르면 조회되지 않는 id가 있다는 뜻
        if(interestedItemCategories.size()!= ids.size()){
            throw new GeneralException(ErrorStatus.ITEM_CATEGORY_NOT_FOUND);
        }
        createMemberCategoryList(newMember,interestedItemCategories);



        return memberRepository.save(newMember);

    }

    private void createMemberCategoryList(Member member, List<Category> categories) {
        for (Category category: categories) {
            MemberCategory memberCategory = MemberCategoryConverter.toMemberCategory(member, category);
            member.getMemberCategories().add(memberCategory);
            category.getMemberCategories().add(memberCategory);
        }
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
        SellerProfile sellerProfile = sellerProfileService.createSellerProfile(member,requestBody);
        member.setSellerProfile(sellerProfile);

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

    public Boolean checkUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public SellerProfile checkSeller (CustomUserDetails userDetails) {

        Member member = userDetails.getMember();
        System.out.println(member.getRole());

        if (member.getRole() == MemberRole.SELLER) {
            return sellerProfileRepository.findByMemberId(member.getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
        } else {
            throw new GeneralException(ErrorStatus.SELLER_REQUIRED);
        }
    }
}
