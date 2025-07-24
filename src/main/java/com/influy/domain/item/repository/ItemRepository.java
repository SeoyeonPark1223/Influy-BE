package com.influy.domain.item.repository;

import com.influy.domain.item.dto.jpql.ItemJPQLResponse;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.ItemStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Integer countBySellerIdAndIsArchivedTrue(Long sellerId);
    Integer countBySellerIdAndIsArchivedFalse(Long sellerId);
    Page<Item> findBySellerIdAndIsArchivedTrue(Long sellerId, Pageable pageable);
    Page<Item> findBySellerIdAndIsArchivedFalse(Long sellerId, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.seller.id = :sellerId AND i.isArchived = false AND i.endDate > :now")
    Page<Item> findOngoingItems(Long sellerId, LocalDateTime now, Pageable pageable);

    @Query("SELECT new com.influy.domain.item.dto.jpql.ItemJPQLResponse(p.isArchived, COUNT(p)) " +
            "FROM Item p WHERE p.seller.id = :sellerId GROUP BY p.isArchived")
    List<ItemJPQLResponse> countBySellerIdGroupByIsArchived(@Param("sellerId") Long sellerId);

    @Query("SELECT i FROM Item i WHERE i.seller.id = :sellerId AND i.talkBoxOpenStatus = 'OPENED'")
    List<Item> findAllBySellerIdAndTalkBoxOpenStatus(Long sellerId);

    @Query("SELECT i FROM Item i WHERE i.endDate > :now AND i.endDate <= :threshold AND i.itemStatus != 'SOLD_OUT'")
    Page<Item> findAllByEndDateAndItemStatus(LocalDateTime now, LocalDateTime threshold, Pageable pageable);

    @Query("""
        SELECT i
        FROM Item i
        LEFT JOIN i.questionList q
        WHERE i.endDate > :now
        AND i.itemStatus != 'SOLD_OUT'
        GROUP BY i
        ORDER BY COUNT(q) DESC
    """)
    Page<Item> findTop3ByQuestionCnt(LocalDateTime now, Pageable pageable);

    @Query("""
        SELECT DISTINCT ic.item FROM ItemCategory ic
        WHERE ic.category.id = :categoryId
          AND ic.item.endDate > :now
          AND i.itemStatus != 'SOLD_OUT'
          ORDER BY ic.item.createdAt DESC
    """)
    Page<Item> findAllByCategoryId(Long categoryId, Pageable pageable, LocalDateTime now);

    @Query("SELECT i FROM Item i WHERE i.endDate > :now AND i.itemStatus != 'SOLD_OUT'")
    Page<Item> findAllNow(Pageable pageable, LocalDateTime now);
}
