package com.GimPay.Integration_APIs.repository;

import com.GimPay.Integration_APIs.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Chercher un paiement par commande (contrainte UNIQUE order_id)
    Optional<Payment> findByOrderId(Long orderId);

    // Chercher par référence marchand (pour le webhook)
    Optional<Payment> findByMerchantReference(String merchantReference);
}