package com.influy.domain.member.entity;

import com.influy.domain.like.entity.Like;
import com.influy.domain.managerProfile.entity.ManagerProfile;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long kakaoId;

    @NotNull
    private String nickname; // 보여질 이름

    private String profileImg;

    @NotNull
    private String username; //=인스타 아이디 강력 추천

    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Nullable
    @Setter
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private SellerProfile sellerProfile;

    @Nullable
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private ManagerProfile managerProfile;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likeList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Question> questionList = new ArrayList<>();

    //추후에 인자 -> dto로 변경
    public Member updateProfile(MemberRequestDTO.UpdateProfile request) {
        if(request.getNickname()!=null){
            this.nickname = request.getNickname();
        }
        if(request.getProfileUrl()!=null){
            this.profileImg = request.getProfileUrl();
        }

        return this;
    }

    public Member updateUsername(String username){
        this.username = username;

        return this;
    }
}
