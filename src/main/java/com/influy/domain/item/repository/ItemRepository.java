package com.influy.domain.item.repository;

import com.influy.domain.item.dto.jpql.ItemJPQLResponse;
import com.influy.domain.item.entity.Item;
import java.util.Optional;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Integer countBySellerIdAndIsArchivedTrue(Long sellerId);
    Integer countBySellerIdAndIsArchivedFalse(Long sellerId);
    @Query("SELECT p.isArchived AS isArchived, COUNT(p) AS count FROM Item p WHERE p.seller.id = :sellerId GROUP BY p.isArchived")
    List<ItemJPQLResponse.IsArchivedItemCount> countBySellerIdGroupByIsArchived(@Param("sellerId") Long sellerId);

    boolean existsByIdAndSellerId(Long itemId, Long sellerId);
    @Query("SELECT i FROM Item i WHERE i.seller.id = :sellerId AND i.talkBoxOpenStatus = 'OPENED'")
    List<Item> findAllBySellerIdAndTalkBoxOpenStatus(@Param("sellerId")Long sellerId);

    @Query("SELECT i FROM Item i WHERE i.endDate > :now AND i.endDate <= :threshold AND i.itemStatus != 'SOLD_OUT' AND i.seller.isPublic = true")
    Page<Item> findAllByEndDateAndItemStatus(@Param("now") LocalDateTime now, @Param("threshold") LocalDateTime threshold, Pageable pageable);

    @Query("""
        SELECT i
        FROM Item i
        LEFT JOIN i.questionList q
        WHERE i.endDate IS NULL OR i.endDate > :now
        AND i.itemStatus != 'SOLD_OUT'
        AND i.seller.isPublic = true
        GROUP BY i
        ORDER BY COUNT(q) DESC
    """)
    Page<Item> findTop3ByQuestionCnt(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("""
        SELECT ic.item FROM ItemCategory ic
        WHERE ic.category.id = :categoryId
          AND ic.item.endDate IS NULL OR ic.item.endDate > :now
          AND ic.item.itemStatus != 'SOLD_OUT'
          AND ic.item.seller.isPublic = true
          AND ic.item.isArchived = false
          ORDER BY ic.item.createdAt DESC
    """)
    Page<Item> findAllByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable, @Param("now") LocalDateTime now);

    @Query("SELECT i FROM Item i WHERE (i.endDate IS NULL OR i.endDate > :now) AND i.itemStatus != 'SOLD_OUT' AND i.seller.isPublic = true AND i.isArchived = false ORDER BY CASE WHEN i.endDate IS NULL THEN 2 ELSE 1 END, i.endDate ASC")
    Page<Item> findAllNow(Pageable pageable, @Param("now") LocalDateTime now);

    Boolean existsByNameContaining(String query);

    Page<Item> findAllByNameContainingOrSeller_Member_UsernameContainingOrSeller_Member_NicknameContainingOrSeller_InstagramContaining(String query, String query1, String query2, String query3, Pageable pageable);

    Optional<Item> findByIdAndSeller(Long itemId, SellerProfile seller);

    @Query(value ="""
    SELECT i AS item,
        SUM(CASE WHEN q.isChecked = false THEN 1 ELSE 0 END) AS newQuestions,
        SUM(CASE WHEN q.isAnswered = false THEN 1 ELSE 0 END) AS pendingQuestions
    FROM Item i
    LEFT JOIN Question q ON q.item = i
    WHERE i.seller.id = :sellerId AND i.talkBoxOpenStatus = 'OPENED'
    GROUP BY i.id
    ORDER BY newQuestions DESC, pendingQuestions DESC
    """, countQuery = """
        SELECT COUNT(i)
        FROM Item i
        WHERE i.seller.id = :sellerId
        """)
    Page<ItemJPQLResponse.ItemWithQuestionStatus> getItemsWithQuestionStatus(@Param("sellerId") Long sellerId, Pageable pageable);

    List<Item> findTop3BySellerId(Long sellerId);

    Boolean existsBySellerId(Long id);

    @Query("""
    SELECT i FROM Item i
    WHERE i.seller.id = :sellerId AND i.isArchived = :isArchived
    ORDER BY
      CASE
        WHEN i.endDate IS NULL THEN 2
        WHEN i.endDate <= :now THEN 3
        ELSE 1
      END,
      i.endDate ASC
    """)
    Page<Item> findAllSortedByEndDate(@Param("sellerId") Long sellerId, @Param("now") LocalDateTime now, @Param("isArchived") Boolean isArchived, Pageable pageable);

    @Query("""
    SELECT i FROM Item i
    WHERE i.seller.id = :sellerId AND i.isArchived = :isArchived
    ORDER BY
      CASE
        WHEN i.endDate IS NULL THEN 2
        WHEN i.endDate <= :now THEN 3
        ELSE 1
      END,
      i.createdAt DESC
    """)
    Page<Item> findAllSortedByCreatedAt(@Param("sellerId") Long sellerId, @Param("now") LocalDateTime now, @Param("isArchived") Boolean isArchived, Pageable pageable);

    @Query("""
    SELECT i FROM Item i
    WHERE i.seller.id = :sellerId AND i.isArchived = :isArchived AND (i.endDate IS NULL OR i.endDate > :now)
    ORDER BY
      CASE
        WHEN i.endDate IS NULL THEN 2
        ELSE 1
      END,
      i.endDate ASC
    """)
    Page<Item> findOngoingItemsSortedByEndDate(@Param("sellerId") Long sellerId, @Param("now") LocalDateTime now, @Param("isArchived") Boolean isArchived, Pageable pageable);

    @Query("""
    SELECT i FROM Item i
    WHERE i.seller.id = :sellerId AND i.isArchived = :isArchived AND (i.endDate IS NULL OR i.endDate > :now)
    """)
    Page<Item> findOngoingItemsSortedByCreatedAt(@Param("sellerId") Long sellerId, @Param("now") LocalDateTime now, @Param("isArchived") Boolean isArchived, Pageable pageable);
}
