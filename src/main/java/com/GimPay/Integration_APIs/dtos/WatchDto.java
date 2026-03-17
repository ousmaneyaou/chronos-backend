package com.GimPay.Integration_APIs.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WatchDto {

    // ── Réponse ───────────────────────────────────────────────────
    public static class Response {
        private Long id;
        private String name;
        private String brand;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private Integer stock;
        private String reference;
        private String material;
        private String movement;
        private String waterResistance;
        private Long categoryId;
        private String categoryName;
        private Boolean active;
        private LocalDateTime createdAt;

        public Response() {}

        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getBrand() { return brand; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public String getImageUrl() { return imageUrl; }
        public Integer getStock() { return stock; }
        public String getReference() { return reference; }
        public String getMaterial() { return material; }
        public String getMovement() { return movement; }
        public String getWaterResistance() { return waterResistance; }
        public Long getCategoryId() { return categoryId; }
        public String getCategoryName() { return categoryName; }
        public Boolean getActive() { return active; }
        public LocalDateTime getCreatedAt() { return createdAt; }

        // Setters
        public void setId(Long id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setBrand(String brand) { this.brand = brand; }
        public void setDescription(String description) { this.description = description; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public void setStock(Integer stock) { this.stock = stock; }
        public void setReference(String reference) { this.reference = reference; }
        public void setMaterial(String material) { this.material = material; }
        public void setMovement(String movement) { this.movement = movement; }
        public void setWaterResistance(String waterResistance) { this.waterResistance = waterResistance; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
        public void setActive(Boolean active) { this.active = active; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        // Builder
        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final Response r = new Response();
            public Builder id(Long v) { r.id = v; return this; }
            public Builder name(String v) { r.name = v; return this; }
            public Builder brand(String v) { r.brand = v; return this; }
            public Builder description(String v) { r.description = v; return this; }
            public Builder price(BigDecimal v) { r.price = v; return this; }
            public Builder imageUrl(String v) { r.imageUrl = v; return this; }
            public Builder stock(Integer v) { r.stock = v; return this; }
            public Builder reference(String v) { r.reference = v; return this; }
            public Builder material(String v) { r.material = v; return this; }
            public Builder movement(String v) { r.movement = v; return this; }
            public Builder waterResistance(String v) { r.waterResistance = v; return this; }
            public Builder categoryId(Long v) { r.categoryId = v; return this; }
            public Builder categoryName(String v) { r.categoryName = v; return this; }
            public Builder active(Boolean v) { r.active = v; return this; }
            public Builder createdAt(LocalDateTime v) { r.createdAt = v; return this; }
            public Response build() { return r; }
        }
    }

    // ── Requête ───────────────────────────────────────────────────
    public static class Request {
        private String name;
        private String brand;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private Integer stock;
        private String reference;
        private String material;
        private String movement;
        private String waterResistance;
        private Long categoryId;

        public Request() {}

        // Getters
        public String getName() { return name; }
        public String getBrand() { return brand; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public String getImageUrl() { return imageUrl; }
        public Integer getStock() { return stock; }
        public String getReference() { return reference; }
        public String getMaterial() { return material; }
        public String getMovement() { return movement; }
        public String getWaterResistance() { return waterResistance; }
        public Long getCategoryId() { return categoryId; }

        // Setters
        public void setName(String name) { this.name = name; }
        public void setBrand(String brand) { this.brand = brand; }
        public void setDescription(String description) { this.description = description; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public void setStock(Integer stock) { this.stock = stock; }
        public void setReference(String reference) { this.reference = reference; }
        public void setMaterial(String material) { this.material = material; }
        public void setMovement(String movement) { this.movement = movement; }
        public void setWaterResistance(String waterResistance) { this.waterResistance = waterResistance; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    }
}