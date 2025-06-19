package com.influy.domain.member.entity;

import com.influy.domain.managerProfile.entity.ManagerProfile;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.userProfile.entity.UserProfile;
import com.influy.global.common.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;

    @NotNull
    private String name; //=실명

    private String profileImg;

    @NotNull
    private String username; //=가입용 email

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @Nullable
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SellerProfile sellerProfile;

    @Nullable
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ManagerProfile managerProfile;

    //추후에 인자 -> dto로 변경
    public Member updateProfile(String profileImg, String nickname, String password) {
        if(nickname!=null){
            this.nickname = nickname;
        }
        if(profileImg!=null){
            this.profileImg = profileImg;
        }

        return this;
    }
}
