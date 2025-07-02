package com.influy.domain.like.repository;

import com.influy.domain.like.entity.Like;
import com.influy.domain.like.entity.LikeStatus;
import com.influy.domain.like.entity.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberIdAndSellerIdAndTargetType(Long memberId, Long sellerId, TargetType targetType);
    Optional<Like> findByMemberIdAndItemIdAndTargetType(Long memberId, Long itemId, TargetType targetType);
    List<Like> findAllByLikeStatus(LikeStatus likeStatus);
}
