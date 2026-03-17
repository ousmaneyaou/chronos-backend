package com.GimPay.Integration_APIs.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    // ── Réponse commande ─────────────────────────────────────────────
    public static class Response {
        private Long id;
        private Long userId;
        private BigDecimal totalAmount;
        private String status;
        private String gimPayOrderId;
        private String gimPayOrderUrl;
        private String gimPayOrderRefId;
        private String shippingAddress;
        private LocalDateTime createdAt;
        private List<ItemResponse> items;
        private PaymentDto.Response payment;

        public Response() {}

        public Long getId() { return id; }
        public Long getUserId() { return userId; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public String getStatus() { return status; }
        public String getGimPayOrderId() { return gimPayOrderId; }
        public String getGimPayOrderUrl() { return gimPayOrderUrl; }
        public String getGimPayOrderRefId() { return gimPayOrderRefId; }
        public String getShippingAddress() { return shippingAddress; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public List<ItemResponse> getItems() { return items; }
        public PaymentDto.Response getPayment() { return payment; }

        public void setId(Long id) { this.id = id; }
        public void setUserId(Long userId) { this.userId = userId; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public void setStatus(String status) { this.status = status; }
        public void setGimPayOrderId(String v) { this.gimPayOrderId = v; }
        public void setGimPayOrderUrl(String v) { this.gimPayOrderUrl = v; }
        public void setGimPayOrderRefId(String v) { this.gimPayOrderRefId = v; }
        public void setShippingAddress(String v) { this.shippingAddress = v; }
        public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
        public void setItems(List<ItemResponse> v) { this.items = v; }
        public void setPayment(PaymentDto.Response v) { this.payment = v; }

        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final Response r = new Response();
            public Builder id(Long v) { r.id = v; return this; }
            public Builder userId(Long v) { r.userId = v; return this; }
            public Builder totalAmount(BigDecimal v) { r.totalAmount = v; return this; }
            public Builder status(String v) { r.status = v; return this; }
            public Builder gimPayOrderId(String v) { r.gimPayOrderId = v; return this; }
            public Builder gimPayOrderUrl(String v) { r.gimPayOrderUrl = v; return this; }
            public Builder gimPayOrderRefId(String v) { r.gimPayOrderRefId = v; return this; }
            public Builder shippingAddress(String v) { r.shippingAddress = v; return this; }
            public Builder createdAt(LocalDateTime v) { r.createdAt = v; return this; }
            public Builder items(List<ItemResponse> v) { r.items = v; return this; }
            public Builder payment(PaymentDto.Response v) { r.payment = v; return this; }
            public Response build() { return r; }
        }
    }

    // ── Article de commande ──────────────────────────────────────────
    public static class ItemResponse {
        private Long id;
        private Long watchId;
        private String watchName;
        private String watchBrand;
        private String watchImage;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;

        public ItemResponse() {}

        public Long getId() { return id; }
        public Long getWatchId() { return watchId; }
        public String getWatchName() { return watchName; }
        public String getWatchBrand() { return watchBrand; }
        public String getWatchImage() { return watchImage; }
        public Integer getQuantity() { return quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public BigDecimal getSubtotal() { return subtotal; }

        public void setId(Long id) { this.id = id; }
        public void setWatchId(Long v) { this.watchId = v; }
        public void setWatchName(String v) { this.watchName = v; }
        public void setWatchBrand(String v) { this.watchBrand = v; }
        public void setWatchImage(String v) { this.watchImage = v; }
        public void setQuantity(Integer v) { this.quantity = v; }
        public void setUnitPrice(BigDecimal v) { this.unitPrice = v; }
        public void setSubtotal(BigDecimal v) { this.subtotal = v; }

        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final ItemResponse r = new ItemResponse();
            public Builder id(Long v) { r.id = v; return this; }
            public Builder watchId(Long v) { r.watchId = v; return this; }
            public Builder watchName(String v) { r.watchName = v; return this; }
            public Builder watchBrand(String v) { r.watchBrand = v; return this; }
            public Builder watchImage(String v) { r.watchImage = v; return this; }
            public Builder quantity(Integer v) { r.quantity = v; return this; }
            public Builder unitPrice(BigDecimal v) { r.unitPrice = v; return this; }
            public Builder subtotal(BigDecimal v) { r.subtotal = v; return this; }
            public ItemResponse build() { return r; }
        }
    }

    // ── Requête création commande ────────────────────────────────────
    // secureHashOverride et dateTimeOverride = optionnels, pour tests HMAC manuels
    public static class CreateRequest {
        private Long userId;
        private String shippingAddress;
        private String secureHashOverride;  // ← optionnel : HMAC manuel pour tests
        private String dateTimeOverride;    // ← optionnel : datetime manuel pour tests

        public CreateRequest() {}

        public Long getUserId() { return userId; }
        public String getShippingAddress() { return shippingAddress; }
        public String getSecureHashOverride() { return secureHashOverride; }
        public String getDateTimeOverride() { return dateTimeOverride; }

        public void setUserId(Long userId) { this.userId = userId; }
        public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
        public void setSecureHashOverride(String secureHashOverride) { this.secureHashOverride = secureHashOverride; }
        public void setDateTimeOverride(String dateTimeOverride) { this.dateTimeOverride = dateTimeOverride; }
    }
}