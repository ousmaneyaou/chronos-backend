package com.GimPay.Integration_APIs.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "watches")
public class Watch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private String imageUrl;

    @Column(nullable = false)
    private Integer stock;

    private String reference;
    private String material;
    private String movement;
    private String waterResistance;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Watch() {}

    // ── Getters ───────────────────────────────────────────────────
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
    public Category getCategory() { return category; }
    public Boolean getActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ── Setters ───────────────────────────────────────────────────
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
    public void setCategory(Category category) { this.category = category; }
    public void setActive(Boolean active) { this.active = active; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ── Builder ───────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Watch w = new Watch();
        public Builder name(String v) { w.name = v; return this; }
        public Builder brand(String v) { w.brand = v; return this; }
        public Builder description(String v) { w.description = v; return this; }
        public Builder price(BigDecimal v) { w.price = v; return this; }
        public Builder imageUrl(String v) { w.imageUrl = v; return this; }
        public Builder stock(Integer v) { w.stock = v; return this; }
        public Builder reference(String v) { w.reference = v; return this; }
        public Builder material(String v) { w.material = v; return this; }
        public Builder movement(String v) { w.movement = v; return this; }
        public Builder waterResistance(String v) { w.waterResistance = v; return this; }
        public Builder category(Category v) { w.category = v; return this; }
        public Builder active(Boolean v) { w.active = v; return this; }
        public Watch build() { return w; }
    }
}