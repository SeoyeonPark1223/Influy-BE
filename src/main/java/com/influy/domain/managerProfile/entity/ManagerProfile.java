package com.influy.domain.managerProfile.entity;

import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerProfile extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "managerProfile")
    private List<SellerProfile> sellerProfile;

}
