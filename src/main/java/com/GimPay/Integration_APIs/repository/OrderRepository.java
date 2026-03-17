package com.GimPay.Integration_APIs.repository;

import com.GimPay.Integration_APIs.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Order> findByGimPayOrderRefId(String refId);
}