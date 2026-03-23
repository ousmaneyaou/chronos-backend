package com.GimPay.Integration_APIs.controllers;

import com.GimPay.Integration_APIs.dtos.PaymentDto;
import com.GimPay.Integration_APIs.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Value("${app.base-url:http://localhost:8020}")
    private String appBaseUrl;

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public PaymentController(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    // ── POST : PayByCard ─────────────────────────────────────────────
    @PostMapping("/pay")
    public ResponseEntity<PaymentDto.Response> payByCard(
            @RequestBody PaymentDto.PayByCardRequest request) {
        return ResponseEntity.ok(orderService.payByCard(request));
    }

    // ── POST : Webhook GIM Pay ───────────────────────────────────────
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhookPost(
            @RequestBody(required = false) String rawBody) {

        log.info("=== WEBHOOK POST REÇU ===");
        log.info("Body brut : {}", rawBody);

        if (rawBody == null || rawBody.isBlank()) {
            log.warn("⚠️ Body vide sur webhook POST");
            return ResponseEntity.ok().build();
        }

        try {
            PaymentDto.GimPayByCardResponse payload =
                    objectMapper.readValue(rawBody, PaymentDto.GimPayByCardResponse.class);
            log.info("Parsed → ref:{} success:{} code:{} sysRef:{}",
                    payload.getMerchantReference(), payload.getSuccess(),
                    payload.getActionCode(), payload.getSystemReference());
            orderService.handleWebhook(payload);
        } catch (Exception e) {
            log.error("❌ Erreur parse webhook POST: {} — body: {}", e.getMessage(), rawBody);
        }

        return ResponseEntity.ok().build();
    }

    // ── GET : Retour PayLink GIM Pay ─────────────────────────────────
    @GetMapping("/webhook")
    public ResponseEntity<Void> webhookGet(
            @RequestParam Map<String, String> params) {

        log.info("=== WEBHOOK GET REÇU === params: {}", params);

        String merchantRef = params.get("MerchantReference");
        String successStr  = params.get("success");
        String systemRef   = params.get("SystemReference");
        String actionCode  = params.get("ActionCode");

        boolean success = "1".equals(successStr) || "true".equalsIgnoreCase(successStr);

        if (merchantRef != null) {
            PaymentDto.GimPayByCardResponse payload = new PaymentDto.GimPayByCardResponse();
            payload.setMerchantReference(merchantRef);
            payload.setSuccess(success);
            payload.setActionCode(actionCode);
            if (systemRef != null) {
                try { payload.setSystemReference(Long.parseLong(systemRef)); }
                catch (NumberFormatException ignored) {}
            }
            log.info("PayLink GET → ref:{} success:{} code:{}", merchantRef, success, actionCode);
            orderService.handleWebhook(payload);
        } else {
            log.warn("⚠️ MerchantReference absent dans webhook GET");
        }

        // Redirection vers le frontend
        String frontendUrl = appBaseUrl.contains("railway")
                ? "https://chronos-frontend-opal.vercel.app/orders"
                : "http://localhost:3000/orders";

        return ResponseEntity.status(302)
                .header("Location", frontendUrl)
                .build();
    }
}