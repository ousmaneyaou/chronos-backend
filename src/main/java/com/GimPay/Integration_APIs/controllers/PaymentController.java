package com.GimPay.Integration_APIs.controllers;

import com.GimPay.Integration_APIs.dtos.PaymentDto;
import com.GimPay.Integration_APIs.services.OrderService;
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

    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ── POST : PayByCard (carte directe) ─────────────────────────────
    @PostMapping("/pay")
    public ResponseEntity<PaymentDto.Response> payByCard(
            @RequestBody PaymentDto.PayByCardRequest request) {
        return ResponseEntity.ok(orderService.payByCard(request));
    }

    // ── POST : Webhook GIM Pay (appelé par le serveur GIM Pay) ───────
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhookPost(
            @RequestBody PaymentDto.GimPayByCardResponse payload) {
        log.info("Webhook POST GIM Pay reçu: merchantRef={}, success={}",
                payload.getMerchantReference(), payload.getSuccess());
        orderService.handleWebhook(payload);
        return ResponseEntity.ok().build();
    }

    // ── GET : Retour PayLink GIM Pay (redirection navigateur) ────────
    // GIM Pay redirige le navigateur ici après paiement via PayLink
    // Paramètres : success=1, MerchantReference=ORDER_XX, SystemReference=...
    @GetMapping("/webhook")
    public ResponseEntity<Void> webhookGet(
            @RequestParam Map<String, String> params) {

        log.info("Webhook GET PayLink reçu: {}", params);

        String merchantRef   = params.get("MerchantReference");
        String successStr    = params.get("success");
        String systemRef     = params.get("SystemReference");
        String actionCode    = params.get("ActionCode");

        boolean success = "1".equals(successStr) || "true".equalsIgnoreCase(successStr);

        if (merchantRef != null) {
            // Construire un objet de réponse et appeler handleWebhook
            PaymentDto.GimPayByCardResponse payload = new PaymentDto.GimPayByCardResponse();
            payload.setMerchantReference(merchantRef);
            payload.setSuccess(success);
            payload.setActionCode(actionCode);
            if (systemRef != null) {
                try { payload.setSystemReference(Long.parseLong(systemRef)); }
                catch (NumberFormatException ignored) {}
            }

            log.info("PayLink GET → ref:{} success:{}", merchantRef, success);
            orderService.handleWebhook(payload);
        }

        // Rediriger vers le frontend
        String frontendUrl = appBaseUrl.contains("railway")
                ? "https://chronos-frontend-opal.vercel.app/orders"
                : "http://localhost:3000/orders";

        return ResponseEntity.status(302)
                .header("Location", frontendUrl)
                .build();
    }
}