package com.influy.domain.like.repository;

import com.influy.domain.like.entity.Like;
import com.influy.domain.like.entity.LikeStatus;
import com.influy.domain.like.entity.TargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberIdAndSellerIdAndTargetType(Long memberId, Long sellerId, TargetType targetType);
    Optional<Like> findByMemberIdAndItemIdAndTargetType(Long memberId, Long itemId, TargetType targetType);
    List<Like> findAllByLikeStatus(LikeStatus likeStatus);
    Integer countBySellerIdAndTargetTypeAndLikeStatus(Long sellerId, TargetType targetType, LikeStatus likeStatus);
    Integer countByItemIdAndTargetTypeAndLikeStatus(Long itemId, TargetType targetType, LikeStatus likeStatus);
    @Query("""
        SELECT l
        FROM Like l
        WHERE l.member.id = :memberId
          AND l.targetType = 'SELLER'
          AND l.likeStatus = 'LIKE'
        ORDER BY (
            SELECT MAX(i.updatedAt)
            FROM Item i
            WHERE i.seller = l.seller
        ) DESC
    """)
    Page<Like> findSellerLikesOrderByRecentItem(Long memberId, Pageable pageable);
    Page<Like> findByMemberIdAndTargetTypeAndLikeStatus(Long memberId, TargetType targetType, LikeStatus likeStatus, Pageable pageable);
}
