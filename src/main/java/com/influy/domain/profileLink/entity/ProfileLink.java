package com.influy.domain.profileLink.entity;

import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileLink extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private SellerProfile seller;

    @NotBlank
    private String linkName;

    @NotBlank
    private String link;

    public ProfileLink setLinkName(String name){
        this.linkName = name;
        return this;
    }

    public ProfileLink setLink(String link){
        this.link = link;
        return this;
    }


}
