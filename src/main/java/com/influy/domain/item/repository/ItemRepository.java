package com.influy.domain.item.repository;

import com.influy.domain.item.dto.jpql.ItemJPQLResponse;
import com.influy.domain.item.entity.Item;
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
}
