package com.influy.domain.seller.entity;

import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.item.entity.Item;
import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCard.entity.QuestionCard;
import com.influy.domain.seller.dto.SellerRequestDTO;
import com.influy.global.common.BaseEntity;
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
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;

    @Builder.Default
    private Boolean isPublic = true;

    private String profileImg;

    private String backgroundImg;

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
    private List<ProfileLink> profileLinkList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Answer> answerList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Item> itemList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<QuestionCard> questionCardList = new ArrayList<>();


    public Seller setProfile(SellerRequestDTO.UpdateProfile requestBody){
        //1차 mvp 이후 QueryDSL 고려
        if(requestBody.getNickname()!=null){
            this.nickname = requestBody.getNickname();
        }
        if(requestBody.getProfileImg()!=null){
            this.profileImg = requestBody.getProfileImg();
        }
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

    public Seller setItemSortType(ItemSortType type){
        this.itemSortType = type;
        return this;
    }

    public Seller setPrimaryAnnouncement(Announcement announcement){
        //기존 최상단 공지와 다른 공지를 등록할 시에 기존 공지 삭제
        if(this.primaryAnnouncement != null && this.primaryAnnouncement!=announcement){
            this.primaryAnnouncement.setIsPrimary(false);
        }
        //새로 등록
        this.primaryAnnouncement = announcement;
        
        return this;
    }
}
