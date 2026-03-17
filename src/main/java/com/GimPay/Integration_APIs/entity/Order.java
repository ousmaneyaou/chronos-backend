package com.GimPay.Integration_APIs.entity;

import com.GimPay.Integration_APIs.enums.OrderStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    private String gimPayOrderId;
    private String gimPayOrderUrl;
    private String gimPayOrderRefId;
    private String shippingAddress;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Order() {}

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public String getGimPayOrderId() { return gimPayOrderId; }
    public String getGimPayOrderUrl() { return gimPayOrderUrl; }
    public String getGimPayOrderRefId() { return gimPayOrderRefId; }
    public String getShippingAddress() { return shippingAddress; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<OrderItem> getItems() { return items; }
    public Payment getPayment() { return payment; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setGimPayOrderId(String v) { this.gimPayOrderId = v; }
    public void setGimPayOrderUrl(String v) { this.gimPayOrderUrl = v; }
    public void setGimPayOrderRefId(String v) { this.gimPayOrderRefId = v; }
    public void setShippingAddress(String v) { this.shippingAddress = v; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }
    public void setItems(List<OrderItem> v) { this.items = v; }
    public void setPayment(Payment v) { this.payment = v; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Order o = new Order();
        public Builder user(User v)              { o.user = v; return this; }
        public Builder totalAmount(BigDecimal v)  { o.totalAmount = v; return this; }
        public Builder status(OrderStatus v)      { o.status = v; return this; }
        public Builder shippingAddress(String v)  { o.shippingAddress = v; return this; }
        public Order build() { return o; }
    }
}