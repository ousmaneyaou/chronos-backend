package com.GimPay.Integration_APIs.dtos;

import java.math.BigDecimal;

public class CartDto {

    public static class Response {
        private Long id;
        private Long watchId;
        private String watchName;
        private String watchBrand;
        private String watchImage;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal subtotal;

        public Response() {}

        // Getters
        public Long getId() { return id; }
        public Long getWatchId() { return watchId; }
        public String getWatchName() { return watchName; }
        public String getWatchBrand() { return watchBrand; }
        public String getWatchImage() { return watchImage; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public Integer getQuantity() { return quantity; }
        public BigDecimal getSubtotal() { return subtotal; }

        // Setters
        public void setId(Long id) { this.id = id; }
        public void setWatchId(Long watchId) { this.watchId = watchId; }
        public void setWatchName(String watchName) { this.watchName = watchName; }
        public void setWatchBrand(String watchBrand) { this.watchBrand = watchBrand; }
        public void setWatchImage(String watchImage) { this.watchImage = watchImage; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

        // Builder
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final Response r = new Response();
            public Builder id(Long v) { r.id = v; return this; }
            public Builder watchId(Long v) { r.watchId = v; return this; }
            public Builder watchName(String v) { r.watchName = v; return this; }
            public Builder watchBrand(String v) { r.watchBrand = v; return this; }
            public Builder watchImage(String v) { r.watchImage = v; return this; }
            public Builder unitPrice(BigDecimal v) { r.unitPrice = v; return this; }
            public Builder quantity(Integer v) { r.quantity = v; return this; }
            public Builder subtotal(BigDecimal v) { r.subtotal = v; return this; }
            public Response build() { return r; }
        }
    }

    public static class AddRequest {
        private Long watchId;
        private Integer quantity;

        public AddRequest() {}
        public Long getWatchId() { return watchId; }
        public Integer getQuantity() { return quantity; }
        public void setWatchId(Long watchId) { this.watchId = watchId; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public static class UpdateRequest {
        private Integer quantity;

        public UpdateRequest() {}
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}