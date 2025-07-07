package com.influy.domain.announcement.entity;

import com.influy.domain.announcement.dto.AnnouncementRequestDTO;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announcement extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private SellerProfile seller;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder.Default
    private Boolean isPrimary = false;

    public Announcement updateAnnouncement(AnnouncementRequestDTO requestDTO, Boolean isPrimary){
        if(requestDTO.getTitle() != null){
            this.title = requestDTO.getTitle();
        }
        if(requestDTO.getContent() != null){
            this.content = requestDTO.getContent();
        }
        if(isPrimary != null){

            if(isPrimary){ //이 공지를 최상단 공지로 등록하는 요청일 경우
                this.seller.setPrimaryAnnouncement(this); //나로 등록
            }
            this.isPrimary = isPrimary;
        }
        return this;
    }

    public Announcement setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
        return this;
    }
}
