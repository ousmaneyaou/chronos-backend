package com.GimPay.Integration_APIs.services;

import com.GimPay.Integration_APIs.dtos.PaymentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GimPayService {

    private static final Logger log = LoggerFactory.getLogger(GimPayService.class);

    @Value("${gimpay.base-url}")
    private String baseUrl;

    @Value("${gimpay.merchant-id}")
    private Long merchantId;

    @Value("${gimpay.terminal-id}")
    private Long terminalId;

    @Value("${gimpay.shared-key}")
    private String sharedKey;

    @Value("${gimpay.currency}")
    private Integer currency;

    @Value("${gimpay.pay-link-type}")
    private Integer payLinkType;

    private final WebClient.Builder webClientBuilder;

    public GimPayService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }


    public String generateSecureHash(String dateTimeLocalTrxn) {
        try {
            String message = "DateTimeLocalTrxn=" + dateTimeLocalTrxn
                    + "&MerchantId=" + merchantId
                    + "&TerminalId=" + terminalId;

            byte[] keyBytes = hexToBytes(sharedKey);

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
            mac.init(keySpec);
            byte[] hashBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) sb.append(String.format("%02x", b));
            String hash = sb.toString();

            log.info("HMAC → message='{}' hash='{}'", message, hash);
            return hash;

        } catch (Exception e) {
            log.error("Erreur HMAC: {}", e.getMessage());
            throw new RuntimeException("Erreur SecureHash", e);
        }
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    //         → chaque appel est unique même à quelques secondes d'écart
    public String generateDateTimeLocalTrxn() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
    }

    // API 1 — InitiateOrder
    public PaymentDto.GimInitiateOrderResponse initiateOrder(  //Appel l’API InitiateOrder de GIM Pay.
            String merchantReference, Double amount, String payerName,
            String callbackUrl, String expiryDateTime,
            String secureHashOverride, String dateTimeOverride) {

        String dt   = dateTimeOverride   != null ? dateTimeOverride   : generateDateTimeLocalTrxn();
        String hash = secureHashOverride != null ? secureHashOverride : generateSecureHash(dt);

        log.info("InitiateOrder → ref:{} dt:{} hash:{}", merchantReference, dt, hash);

        PaymentDto.GimInitiateOrderRequest req = new PaymentDto.GimInitiateOrderRequest();
        req.setAmountTrxn(amount);
        req.setCurrency(currency);
        req.setExpiryDateTime(expiryDateTime);
        req.setMerchantReference(merchantReference);
        req.setPayerName(payerName);
        req.setCallBackUrl(callbackUrl);
        req.setMaxNumberOfPayment(1);
        req.setMessage("");
        req.setPayLinkType(payLinkType);
        req.setTokenizationCustomerOperatorId(null);
        req.setOrderReceiptPath("");
        req.setOrderReceiptName("");
        req.setTerminalId(terminalId);
        req.setMerchantId(merchantId);
        req.setSecureHash(hash);
        req.setDateTimeLocalTrxn(dt);
        req.setHostName(0);
        req.setCorrelationId(null);

        try {
            PaymentDto.GimInitiateOrderResponse resp = webClientBuilder.build()
                    .post().uri(baseUrl + "/InitiateOrder")
                    .header("Content-Type", "application/json")
                    .bodyValue(req).retrieve()
                    .onStatus(HttpStatusCode::isError, r ->
                            r.bodyToMono(String.class).map(b -> {
                                log.error("InitiateOrder {} → {}", r.statusCode(), b);
                                return new RuntimeException(r.statusCode() + ": " + b);
                            })
                    )
                    .bodyToMono(PaymentDto.GimInitiateOrderResponse.class).block(); //conversion de json

            log.info("InitiateOrder ← success:{} orderId:{} msg:{}",
                    resp != null ? resp.getSuccess() : "null",
                    resp != null ? resp.getOrderId() : "null",
                    resp != null ? resp.getMessage() : "null");
            return resp;

        } catch (WebClientResponseException e) {
            log.error("InitiateOrder {} : {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("InitiateOrder: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("InitiateOrder: {}", e.getMessage());
            throw new RuntimeException("InitiateOrder: " + e.getMessage());
        }
    }

    public PaymentDto.GimInitiateOrderResponse initiateOrder(
            String merchantReference, Double amount, String payerName,
            String callbackUrl, String expiryDateTime) {
        return initiateOrder(merchantReference, amount, payerName, callbackUrl, expiryDateTime, null, null);
    }

    // API 2 — PayByCard
    public PaymentDto.GimPayByCardResponse payByCard(  //Appel l’API PayByCard.
            String pan, String dateExpiration, String cvv2,
            Double amount, String merchantReference, String returnUrl,
            Boolean disable3ds, String secureHashOverride, String dateTimeOverride) {

        String dt   = dateTimeOverride   != null ? dateTimeOverride   : generateDateTimeLocalTrxn();
        String hash = secureHashOverride != null ? secureHashOverride : generateSecureHash(dt);

        log.info("PayByCard → ref:{} 3DS:{} dt:{} hash:{}", merchantReference, disable3ds ? "OFF" : "ON", dt, hash);

        PaymentDto.GimPayByCardRequest req = new PaymentDto.GimPayByCardRequest();
        req.setPan(pan);
        req.setDateExpiration(dateExpiration);
        req.setCvv2(cvv2);
        req.setAmountTrxn(amount);
        req.setIsWebRequest(true);
        req.setCurrencyCodeTrxn(currency.toString());
        req.setMerchantReference(merchantReference);
        req.setDisable3DS(disable3ds);
        req.setTerminalId(terminalId);
        req.setMerchantId(merchantId);
        req.setDateTimeLocalTrxn(dt);
        req.setReturnURL(returnUrl);
        req.setSecureHash(hash);

        try {
            PaymentDto.GimPayByCardResponse resp = webClientBuilder.build()
                    .post().uri(baseUrl + "/PayByCard")
                    .header("Content-Type", "application/json")
                    .bodyValue(req).retrieve()
                    .onStatus(HttpStatusCode::isError, r ->
                            r.bodyToMono(String.class).map(b -> {
                                log.error("PayByCard {} → {}", r.statusCode(), b);
                                return new RuntimeException(r.statusCode() + ": " + b);
                            })
                    )
                    .bodyToMono(PaymentDto.GimPayByCardResponse.class).block();

            log.info("PayByCard ← success:{} actionCode:{} challenge:{}",
                    resp != null ? resp.getSuccess() : "null",
                    resp != null ? resp.getActionCode() : "null",
                    resp != null ? resp.getChallengeRequired() : "null");
            return resp;

        } catch (WebClientResponseException e) {
            log.error("PayByCard {} : {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("PayByCard: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("PayByCard: {}", e.getMessage());
            throw new RuntimeException("PayByCard: " + e.getMessage());
        }
    }

    public PaymentDto.GimPayByCardResponse payByCard(
            String pan, String dateExpiration, String cvv2,
            Double amount, String merchantReference, String returnUrl, Boolean disable3ds) {
        return payByCard(pan, dateExpiration, cvv2, amount, merchantReference, returnUrl, disable3ds, null, null);
    }
}