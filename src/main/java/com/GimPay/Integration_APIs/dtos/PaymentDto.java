package com.GimPay.Integration_APIs.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDto {

    // ── Réponse paiement vers le frontend ────────────────────────────
    public static class Response {
        private Long id;
        private BigDecimal amount;
        private String status;
        private String method;
        private String actionCode;
        private String systemReference;
        private Boolean challengeRequired;
        private String threeDsUrl;
        private String threeDsTxnId;
        private LocalDateTime createdAt;

        public Response() {}

        public Long getId() { return id; }
        public BigDecimal getAmount() { return amount; }
        public String getStatus() { return status; }
        public String getMethod() { return method; }
        public String getActionCode() { return actionCode; }
        public String getSystemReference() { return systemReference; }
        public Boolean getChallengeRequired() { return challengeRequired; }
        public String getThreeDsUrl() { return threeDsUrl; }
        public String getThreeDsTxnId() { return threeDsTxnId; }
        public LocalDateTime getCreatedAt() { return createdAt; }

        public void setId(Long id) { this.id = id; }
        public void setAmount(BigDecimal v) { this.amount = v; }
        public void setStatus(String v) { this.status = v; }
        public void setMethod(String v) { this.method = v; }
        public void setActionCode(String v) { this.actionCode = v; }
        public void setSystemReference(String v) { this.systemReference = v; }
        public void setChallengeRequired(Boolean v) { this.challengeRequired = v; }
        public void setThreeDsUrl(String v) { this.threeDsUrl = v; }
        public void setThreeDsTxnId(String v) { this.threeDsTxnId = v; }
        public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final Response r = new Response();
            public Builder id(Long v) { r.id = v; return this; }
            public Builder amount(BigDecimal v) { r.amount = v; return this; }
            public Builder status(String v) { r.status = v; return this; }
            public Builder method(String v) { r.method = v; return this; }
            public Builder actionCode(String v) { r.actionCode = v; return this; }
            public Builder systemReference(String v) { r.systemReference = v; return this; }
            public Builder challengeRequired(Boolean v) { r.challengeRequired = v; return this; }
            public Builder threeDsUrl(String v) { r.threeDsUrl = v; return this; }
            public Builder threeDsTxnId(String v) { r.threeDsTxnId = v; return this; }
            public Builder createdAt(LocalDateTime v) { r.createdAt = v; return this; }
            public Response build() { return r; }
        }
    }

    // ── Requête paiement par carte depuis le frontend ─────────────────
    public static class PayByCardRequest {
        private Long orderId;
        private String pan;
        private String dateExpiration;
        private String cvv2;
        private Boolean disable3ds;
        private String returnUrl;
        // Optionnel — pour tests HMAC manuels
        private String secureHashOverride;
        private String dateTimeOverride;

        public PayByCardRequest() {}

        public Long getOrderId() { return orderId; }
        public String getPan() { return pan; }
        public String getDateExpiration() { return dateExpiration; }
        public String getCvv2() { return cvv2; }
        public Boolean getDisable3ds() { return disable3ds; }
        public String getReturnUrl() { return returnUrl; }
        public String getSecureHashOverride() { return secureHashOverride; }
        public String getDateTimeOverride() { return dateTimeOverride; }

        public void setOrderId(Long v) { this.orderId = v; }
        public void setPan(String v) { this.pan = v; }
        public void setDateExpiration(String v) { this.dateExpiration = v; }
        public void setCvv2(String v) { this.cvv2 = v; }
        public void setDisable3ds(Boolean v) { this.disable3ds = v; }
        public void setReturnUrl(String v) { this.returnUrl = v; }
        public void setSecureHashOverride(String v) { this.secureHashOverride = v; }
        public void setDateTimeOverride(String v) { this.dateTimeOverride = v; }
    }

    // ── GIM Pay : corps requête InitiateOrder ─────────────────────────
    public static class GimInitiateOrderRequest {
        @JsonProperty("AmountTrxn")       private Double amountTrxn;
        @JsonProperty("Currency")          private Integer currency;
        @JsonProperty("ExpiryDateTime")    private String expiryDateTime;
        @JsonProperty("MerchantReference") private String merchantReference;
        @JsonProperty("PayerName")         private String payerName;
        @JsonProperty("CallBackUrl")       private String callBackUrl;
        @JsonProperty("MaxNumberOfPayment") private Integer maxNumberOfPayment;
        @JsonProperty("Message")           private String message;
        @JsonProperty("PayLinkType")       private Integer payLinkType;
        @JsonProperty("TokenizationCustomerOperatorId") private Object tokenizationCustomerOperatorId;
        @JsonProperty("OrderReceiptPath")  private String orderReceiptPath;
        @JsonProperty("OrderReceiptName")  private String orderReceiptName;
        @JsonProperty("TerminalId")        private Long terminalId;
        @JsonProperty("MerchantId")        private Long merchantId;
        @JsonProperty("SecureHash")        private String secureHash;
        @JsonProperty("DateTimeLocalTrxn") private String dateTimeLocalTrxn;
        @JsonProperty("HostName")          private Integer hostName;
        @JsonProperty("CorrelationId")     private Object correlationId;

        public GimInitiateOrderRequest() {}

        public void setAmountTrxn(Double v) { this.amountTrxn = v; }
        public void setCurrency(Integer v) { this.currency = v; }
        public void setExpiryDateTime(String v) { this.expiryDateTime = v; }
        public void setMerchantReference(String v) { this.merchantReference = v; }
        public void setPayerName(String v) { this.payerName = v; }
        public void setCallBackUrl(String v) { this.callBackUrl = v; }
        public void setMaxNumberOfPayment(Integer v) { this.maxNumberOfPayment = v; }
        public void setMessage(String v) { this.message = v; }
        public void setPayLinkType(Integer v) { this.payLinkType = v; }
        public void setTokenizationCustomerOperatorId(Object v) { this.tokenizationCustomerOperatorId = v; }
        public void setOrderReceiptPath(String v) { this.orderReceiptPath = v; }
        public void setOrderReceiptName(String v) { this.orderReceiptName = v; }
        public void setTerminalId(Long v) { this.terminalId = v; }
        public void setMerchantId(Long v) { this.merchantId = v; }
        public void setSecureHash(String v) { this.secureHash = v; }
        public void setDateTimeLocalTrxn(String v) { this.dateTimeLocalTrxn = v; }
        public void setHostName(Integer v) { this.hostName = v; }
        public void setCorrelationId(Object v) { this.correlationId = v; }
    }

    // ── GIM Pay : corps requête PayByCard ─────────────────────────────
    public static class GimPayByCardRequest {
        @JsonProperty("PAN")               private String pan;
        @JsonProperty("DateExpiration")    private String dateExpiration;
        @JsonProperty("CVV2")              private String cvv2;
        @JsonProperty("AmountTrxn")        private Double amountTrxn;
        @JsonProperty("IsWebRequest")      private Boolean isWebRequest;
        @JsonProperty("CurrencyCodeTrxn")  private String currencyCodeTrxn;
        @JsonProperty("MerchantReference") private String merchantReference;
        @JsonProperty("Disable3DS")        private Boolean disable3DS;
        @JsonProperty("TerminalId")        private Long terminalId;
        @JsonProperty("MerchantId")        private Long merchantId;
        @JsonProperty("DateTimeLocalTrxn") private String dateTimeLocalTrxn;
        @JsonProperty("ReturnURL")         private String returnURL;
        @JsonProperty("SecureHash")        private String secureHash;

        public GimPayByCardRequest() {}

        public void setPan(String v) { this.pan = v; }
        public void setDateExpiration(String v) { this.dateExpiration = v; }
        public void setCvv2(String v) { this.cvv2 = v; }
        public void setAmountTrxn(Double v) { this.amountTrxn = v; }
        public void setIsWebRequest(Boolean v) { this.isWebRequest = v; }
        public void setCurrencyCodeTrxn(String v) { this.currencyCodeTrxn = v; }
        public void setMerchantReference(String v) { this.merchantReference = v; }
        public void setDisable3DS(Boolean v) { this.disable3DS = v; }
        public void setTerminalId(Long v) { this.terminalId = v; }
        public void setMerchantId(Long v) { this.merchantId = v; }
        public void setDateTimeLocalTrxn(String v) { this.dateTimeLocalTrxn = v; }
        public void setReturnURL(String v) { this.returnURL = v; }
        public void setSecureHash(String v) { this.secureHash = v; }
    }

    // ── GIM Pay : réponse InitiateOrder ───────────────────────────────
    public static class GimInitiateOrderResponse {
        @JsonProperty("OrderId")     private String orderId;
        @JsonProperty("OrderURL")    private String orderURL;
        @JsonProperty("OrderRefId")  private String orderRefId;
        @JsonProperty("Success")     private Boolean success;
        @JsonProperty("Message")     private String message;
        @JsonProperty("StatusCode")  private Integer statusCode;

        public GimInitiateOrderResponse() {}

        public String getOrderId() { return orderId; }
        public String getOrderURL() { return orderURL; }
        public String getOrderRefId() { return orderRefId; }
        public Boolean getSuccess() { return success; }
        public String getMessage() { return message; }
        public Integer getStatusCode() { return statusCode; }

        public void setOrderId(String v) { this.orderId = v; }
        public void setOrderURL(String v) { this.orderURL = v; }
        public void setOrderRefId(String v) { this.orderRefId = v; }
        public void setSuccess(Boolean v) { this.success = v; }
        public void setMessage(String v) { this.message = v; }
        public void setStatusCode(Integer v) { this.statusCode = v; }
    }

    // ── GIM Pay : réponse PayByCard ───────────────────────────────────
    public static class GimPayByCardResponse {
        @JsonProperty("ActionCode")        private String actionCode;
        @JsonProperty("SystemReference")   private Long systemReference;
        @JsonProperty("MerchantReference") private String merchantReference;
        @JsonProperty("ChallengeRequired") private Boolean challengeRequired;
        @JsonProperty("ThreeDSUrl")        private String threeDSUrl;
        @JsonProperty("ThreeDSTxnId")      private String threeDSTxnId;
        @JsonProperty("Success")           private Boolean success;
        @JsonProperty("Message")           private String message;
        @JsonProperty("StatusCode")        private Integer statusCode;

        public GimPayByCardResponse() {}

        public String getActionCode() { return actionCode; }
        public Long getSystemReference() { return systemReference; }
        public String getMerchantReference() { return merchantReference; }
        public Boolean getChallengeRequired() { return challengeRequired; }
        public String getThreeDSUrl() { return threeDSUrl; }
        public String getThreeDSTxnId() { return threeDSTxnId; }
        public Boolean getSuccess() { return success; }
        public String getMessage() { return message; }
        public Integer getStatusCode() { return statusCode; }

        public void setActionCode(String v) { this.actionCode = v; }
        public void setSystemReference(Long v) { this.systemReference = v; }
        public void setMerchantReference(String v) { this.merchantReference = v; }
        public void setChallengeRequired(Boolean v) { this.challengeRequired = v; }
        public void setThreeDSUrl(String v) { this.threeDSUrl = v; }
        public void setThreeDSTxnId(String v) { this.threeDSTxnId = v; }
        public void setSuccess(Boolean v) { this.success = v; }
        public void setMessage(String v) { this.message = v; }
        public void setStatusCode(Integer v) { this.statusCode = v; }
    }
}