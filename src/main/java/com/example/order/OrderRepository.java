package com.example.order;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.isOrdered = false")
    List<Order> findCartItemsByUserId(@Param("userId") String userId);
    List<Order> findByUserIdAndIsOrderedTrue(String userId);
    void deleteByProductIdInAndUserId(List<Long> productIds, String userId);

    // 중복을 고려하여 장바구니에 담긴 상품의 총 개수를 계산
    @Query("SELECT COUNT(DISTINCT o.productId) FROM Order o WHERE o.userId = :userId AND o.isOrdered = false")
    int countTotalProductsByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.isOrdered = true WHERE o.userId = :userId AND o.productId IN :selectedProductIds")
    void updateOrder(@Param("selectedProductIds") List<Long> selectedProductIds, @Param("userId") Long userId);
}