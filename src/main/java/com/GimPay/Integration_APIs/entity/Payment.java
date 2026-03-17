package com.GimPay.Integration_APIs.entity;

import com.GimPay.Integration_APIs.enums.PaymentMethod;
import com.GimPay.Integration_APIs.enums.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private String actionCode;
    private String systemReference;
    private String merchantReference;
    private Boolean challengeRequired;
    private String threeDsUrl;
    private String threeDsTxnId;
    private Boolean disable3ds;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Payment() {}

    // Getters
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public BigDecimal getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public PaymentMethod getMethod() { return method; }
    public String getActionCode() { return actionCode; }
    public String getSystemReference() { return systemReference; }
    public String getMerchantReference() { return merchantReference; }
    public Boolean getChallengeRequired() { return challengeRequired; }
    public String getThreeDsUrl() { return threeDsUrl; }
    public String getThreeDsTxnId() { return threeDsTxnId; }
    public Boolean getDisable3ds() { return disable3ds; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOrder(Order order) { this.order = order; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public void setMethod(PaymentMethod method) { this.method = method; }
    public void setActionCode(String actionCode) { this.actionCode = actionCode; }
    public void setSystemReference(String systemReference) { this.systemReference = systemReference; }
    public void setMerchantReference(String merchantReference) { this.merchantReference = merchantReference; }
    public void setChallengeRequired(Boolean challengeRequired) { this.challengeRequired = challengeRequired; }
    public void setThreeDsUrl(String threeDsUrl) { this.threeDsUrl = threeDsUrl; }
    public void setThreeDsTxnId(String threeDsTxnId) { this.threeDsTxnId = threeDsTxnId; }
    public void setDisable3ds(Boolean disable3ds) { this.disable3ds = disable3ds; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Payment p = new Payment();
        public Builder order(Order v) { p.order = v; return this; }
        public Builder amount(BigDecimal v) { p.amount = v; return this; }
        public Builder status(PaymentStatus v) { p.status = v; return this; }
        public Builder method(PaymentMethod v) { p.method = v; return this; }
        public Builder merchantReference(String v) { p.merchantReference = v; return this; }
        public Builder disable3ds(Boolean v) { p.disable3ds = v; return this; }
        public Payment build() { return p; }
    }
}