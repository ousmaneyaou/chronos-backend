package com.GimPay.Integration_APIs.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "watch_id", nullable = false)
    private Watch watch;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    public OrderItem() {}

    // Getters
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public Watch getWatch() { return watch; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOrder(Order order) { this.order = order; }
    public void setWatch(Watch watch) { this.watch = watch; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final OrderItem i = new OrderItem();
        public Builder order(Order v) { i.order = v; return this; }
        public Builder watch(Watch v) { i.watch = v; return this; }
        public Builder quantity(Integer v) { i.quantity = v; return this; }
        public Builder unitPrice(BigDecimal v) { i.unitPrice = v; return this; }
        public Builder subtotal(BigDecimal v) { i.subtotal = v; return this; }
        public OrderItem build() { return i; }
    }
}