package com.influy.domain.like.repository;

import com.influy.domain.item.entity.Item;
import com.influy.domain.like.entity.Like;
import com.influy.domain.like.entity.TargetType;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberAndSellerAndTargetType(Member member, SellerProfile seller, TargetType targetType);
    Optional<Like> findByMemberAndItemAndTargetType(Member member, Item item, TargetType targetType);
}
