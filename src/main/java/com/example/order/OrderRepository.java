package com.example.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);

    void deleteByProductIdInAndUserId(List<Long> productIds, String userId);

    // 중복을 고려하여 장바구니에 담긴 상품의 총 개수를 계산
    @Query("SELECT COUNT(DISTINCT c.productId) FROM Order c WHERE c.userId = :userId")
    int countTotalProductsByUserId(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE Order o SET o.isOrdered = true WHERE o.userId = :userId AND o.productId IN :selectedProductIds")
    void updateOrder(@Param("selectedProductIds") List<Long> selectedProductIds, @Param("userId") Long userId);
}