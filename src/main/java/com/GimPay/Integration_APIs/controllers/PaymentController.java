package com.GimPay.Integration_APIs.controllers;

import com.GimPay.Integration_APIs.dtos.PaymentDto;
import com.GimPay.Integration_APIs.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final OrderService orderService;

    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/pay")
    public ResponseEntity<PaymentDto.Response> payByCard(
            @RequestBody PaymentDto.PayByCardRequest request) {
        return ResponseEntity.ok(orderService.payByCard(request));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody PaymentDto.GimPayByCardResponse payload) {
        log.info("Webhook GIM Pay reçu: merchantRef={}, success={}",
                payload.getMerchantReference(), payload.getSuccess());
        orderService.handleWebhook(payload);
        return ResponseEntity.ok().build();
    }

}