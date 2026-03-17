package com.GimPay.Integration_APIs.repository;


import com.GimPay.Integration_APIs.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserIdAndWatchId(Long userId, Long watchId);
    void deleteByUserId(Long userId);
}