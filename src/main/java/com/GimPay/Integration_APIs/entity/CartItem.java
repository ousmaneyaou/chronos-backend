package com.GimPay.Integration_APIs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "watch_id", nullable = false)
    private Watch watch;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @PrePersist
    protected void onCreate() { addedAt = LocalDateTime.now(); }

    public CartItem() {}

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Watch getWatch() { return watch; }
    public Integer getQuantity() { return quantity; }
    public LocalDateTime getAddedAt() { return addedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setWatch(Watch watch) { this.watch = watch; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final CartItem c = new CartItem();
        public Builder user(User v) { c.user = v; return this; }
        public Builder watch(Watch v) { c.watch = v; return this; }
        public Builder quantity(Integer v) { c.quantity = v; return this; }
        public CartItem build() { return c; }
    }
}