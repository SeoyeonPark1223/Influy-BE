package com.influy.domain.sellerProfile.entity;

import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.item.entity.Item;
import com.influy.domain.managerProfile.entity.ManagerProfile;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.global.common.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerProfile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable   //매니저가 임의로 셀러를 만드는 경우
    @OneToOne
    private Member member;

    /**
     * 일단 1차 MVP 문제 없도록 넣어두지만
     * 2차 MVP때부터는 프론트에도 requestBody, responseBody에 nickname, profilePic 빠진다고 알리기
     * seller 페이지에서도 회원 기본 정보(닉네임 등)는 member API를 통해 받아와야하지 한번에 주지않음!!
     * seller API를 셀러 정보 API 보다는 마켓 API정도로 생각하는게 맞음
     */
    @NotNull
    @Builder.Default
    private String nickname = "2차 MVP 수정때문에 임시 닉네임사용";

    private String profileImg;


    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_profile_id")
    private ManagerProfile managerProfile;

    @Builder.Default
    private Boolean isPublic = true;

    private String backgroundImg;

    //@Embedded 고려
    private String instagram;

    private String tiktok;

    private String youtube;

    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ItemSortType itemSortType = ItemSortType.CREATE_DATE;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Announcement> announcementList = new ArrayList<>();

    //삭제하기
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_announcement_id", unique = true)
    private Announcement primaryAnnouncement;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Answer> answerList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Item> itemList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FaqCard> faqCardList = new ArrayList<>();


    public SellerProfile setProfile(SellerProfileRequestDTO.UpdateProfile requestBody){
        //1차 mvp 이후 QueryDSL 고려
        if(requestBody.getBackgroundImg()!=null){
            this.backgroundImg = requestBody.getBackgroundImg();
        }
        if(requestBody.getInstagram()!=null){
            this.instagram = requestBody.getInstagram();
        }
        if(requestBody.getTiktok()!=null){
            this.tiktok = requestBody.getTiktok();
        }
        if(requestBody.getYoutube()!=null){
            this.youtube = requestBody.getYoutube();
        }
        if(requestBody.getEmail()!=null){
            this.email = requestBody.getEmail();
        }
        if(requestBody.getIsPublic()!=null){
            this.isPublic = requestBody.getIsPublic();
        }

        return this;
    }

    public SellerProfile setItemSortType(ItemSortType type){
        this.itemSortType = type;
        return this;
    }

    public SellerProfile setPrimaryAnnouncement(Announcement announcement){
        //기존 최상단 공지와 다른 공지를 등록할 시에 기존 공지 삭제
        if(this.primaryAnnouncement != null && this.primaryAnnouncement!=announcement){
            this.primaryAnnouncement.setIsPrimary(false);
        }
        //새로 등록
        this.primaryAnnouncement = announcement;
        
        return this;
    }
}
