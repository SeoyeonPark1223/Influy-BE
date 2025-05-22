package com.influy.domain.item.repository;

import com.influy.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Integer countBySellerIdAndIsArchivedTrue(Long sellerId);
    Integer countBySellerIdAndIsArchivedFalse(Long sellerId);
    Page<Item> findBySellerIdAndIsArchivedTrue(Long sellerId, Pageable pageable);
    Page<Item> findBySellerIdAndIsArchivedFalse(Long sellerId, Pageable pageable);
}
