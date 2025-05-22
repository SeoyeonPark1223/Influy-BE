package com.influy.domain.item.repository;

import com.influy.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findBySellerIdAndIsArchivedTrueOrderByCreatedAtDesc(Long sellerId);
    List<Item> findBySellerIdAndIsArchivedFalseOrderByCreatedAtDesc(Long sellerId);
    Integer countBySellerIdAndIsArchivedTrue(Long sellerId);
    Integer countBySellerIdAndIsArchivedFalse(Long sellerId);
}
