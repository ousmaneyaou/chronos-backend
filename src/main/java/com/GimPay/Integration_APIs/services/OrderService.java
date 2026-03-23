package com.GimPay.Integration_APIs.services;

import com.GimPay.Integration_APIs.dtos.OrderDto;
import com.GimPay.Integration_APIs.dtos.PaymentDto;
import com.GimPay.Integration_APIs.entity.CartItem;
import com.GimPay.Integration_APIs.entity.Order;
import com.GimPay.Integration_APIs.entity.OrderItem;
import com.GimPay.Integration_APIs.entity.Payment;
import com.GimPay.Integration_APIs.entity.User;
import com.GimPay.Integration_APIs.entity.Watch;
import com.GimPay.Integration_APIs.enums.OrderStatus;
import com.GimPay.Integration_APIs.enums.PaymentMethod;
import com.GimPay.Integration_APIs.enums.PaymentStatus;
import com.GimPay.Integration_APIs.repository.CartItemRepository;
import com.GimPay.Integration_APIs.repository.OrderItemRepository;
import com.GimPay.Integration_APIs.repository.OrderRepository;
import com.GimPay.Integration_APIs.repository.PaymentRepository;
import com.GimPay.Integration_APIs.repository.UserRepository;
import com.GimPay.Integration_APIs.repository.WatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Value("${app.base-url:http://localhost:8020}")
    private String appBaseUrl;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final WatchRepository watchRepository;
    private final PaymentRepository paymentRepository;
    private final GimPayService gimPayService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CartItemRepository cartItemRepository,
                        UserRepository userRepository,
                        WatchRepository watchRepository,
                        PaymentRepository paymentRepository,
                        GimPayService gimPayService) {
        this.orderRepository     = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository  = cartItemRepository;
        this.userRepository      = userRepository;
        this.watchRepository     = watchRepository;
        this.paymentRepository   = paymentRepository;
        this.gimPayService       = gimPayService;
    }

    @Transactional
    public OrderDto.Response createOrder(OrderDto.CreateRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + req.getUserId()));

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) throw new RuntimeException("Le panier est vide");

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cartItems)
            total = total.add(ci.getWatch().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));

        Order order = Order.builder()
                .user(user).totalAmount(total)
                .status(OrderStatus.PENDING)
                .shippingAddress(req.getShippingAddress())
                .build();
        order = orderRepository.save(order);

        for (CartItem ci : cartItems) {
            Watch watch = ci.getWatch();
            if (watch.getStock() < ci.getQuantity())
                throw new RuntimeException("Stock insuffisant: " + watch.getName());
            watch.setStock(watch.getStock() - ci.getQuantity());
            watchRepository.save(watch);
            orderItemRepository.save(OrderItem.builder()
                    .order(order).watch(watch).quantity(ci.getQuantity())
                    .unitPrice(watch.getPrice())
                    .subtotal(watch.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                    .build());
        }
        cartItemRepository.deleteByUserId(user.getId());

        String ref         = "ORDER_" + order.getId() + "_" + UUID.randomUUID().toString().substring(0, 8);
        String expiry      = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        // ← CallbackUrl = endpoint GET sur le backend
        // GIM Pay appellera ce lien après paiement PayLink
        // Le controller GET redirige ensuite vers le frontend
        String callbackUrl = appBaseUrl + "/api/payment/webhook";
        log.info("Webhook URL = {}", callbackUrl);

        try {
            PaymentDto.GimInitiateOrderResponse gimResp = gimPayService.initiateOrder(
                    ref, total.doubleValue(),
                    user.getFirstName() + " " + user.getLastName(),
                    callbackUrl, expiry,
                    req.getSecureHashOverride(), req.getDateTimeOverride());

            if (gimResp != null && Boolean.TRUE.equals(gimResp.getSuccess())) {
                order.setGimPayOrderId(gimResp.getOrderId());
                order.setGimPayOrderUrl(gimResp.getOrderURL());
                order.setGimPayOrderRefId(gimResp.getOrderRefId());
                order.setStatus(OrderStatus.PAYMENT_INITIATED);
                order = orderRepository.save(order);
                log.info("InitiateOrder OK → id:{} url:{}", gimResp.getOrderId(), gimResp.getOrderURL());
            } else {
                log.warn("InitiateOrder échoué: {}", gimResp != null ? gimResp.getMessage() : "null");
            }
        } catch (Exception e) {
            log.warn("InitiateOrder exception: {}", e.getMessage());
        }

        return toDto(orderRepository.findById(order.getId()).orElse(order));
    }

    @Transactional
    public PaymentDto.Response payByCard(PaymentDto.PayByCardRequest req) {
        Order order = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new RuntimeException("Commande non trouvée: " + req.getOrderId()));

        String ref       = "PAY_" + order.getId() + "_" + UUID.randomUUID().toString().substring(0, 8);
        String returnUrl = req.getReturnUrl() != null
                ? req.getReturnUrl()
                : appBaseUrl + "/payment/result";

        PaymentDto.GimPayByCardResponse gimResp = gimPayService.payByCard(
                req.getPan(), req.getDateExpiration(), req.getCvv2(),
                order.getTotalAmount().doubleValue(), ref, returnUrl, req.getDisable3ds(),
                req.getSecureHashOverride(), req.getDateTimeOverride());

        Optional<Payment> existingOpt = paymentRepository.findByOrderId(order.getId());
        Payment payment = existingOpt.orElse(Payment.builder()
                .order(order).amount(order.getTotalAmount()).build());

        payment.setMerchantReference(ref);
        payment.setDisable3ds(req.getDisable3ds());
        payment.setMethod(Boolean.TRUE.equals(req.getDisable3ds())
                ? PaymentMethod.CARD_3DS_OFF : PaymentMethod.CARD_3DS_ON);

        if (gimResp != null) {
            payment.setActionCode(gimResp.getActionCode());
            payment.setSystemReference(gimResp.getSystemReference() != null
                    ? gimResp.getSystemReference().toString() : null);
            payment.setChallengeRequired(gimResp.getChallengeRequired());
            payment.setThreeDsUrl(gimResp.getThreeDSUrl());
            payment.setThreeDsTxnId(gimResp.getThreeDSTxnId());

            if (Boolean.TRUE.equals(gimResp.getSuccess())) {
                if (Boolean.TRUE.equals(gimResp.getChallengeRequired())) {
                    payment.setStatus(PaymentStatus.SUCCESS);
                    order.setStatus(OrderStatus.PAID);
                    log.info("✅ 3DS ON → Challenge approuvé → PAID directement");
                } else {
                    payment.setStatus(PaymentStatus.SUCCESS);
                    order.setStatus(OrderStatus.PAID);
                    log.info("✅ 3DS OFF → Paiement direct confirmé → PAID");
                }
            } else {
                String actionCode = gimResp.getActionCode();
                if ("909".equals(actionCode)) {
                    payment.setStatus(PaymentStatus.INITIATED);
                    order.setStatus(OrderStatus.PAYMENT_INITIATED);
                    log.info("Code 909 (carte test UAT) → PAYMENT_INITIATED");
                } else {
                    payment.setStatus(PaymentStatus.FAILED);
                    order.setStatus(OrderStatus.FAILED);
                    log.warn("Paiement échoué → code:{} msg:{}", actionCode, gimResp.getMessage());
                }
            }
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.FAILED);
            log.warn("Réponse GIM Pay nulle");
        }

        orderRepository.save(order);
        payment = paymentRepository.save(payment);
        return toPaymentDto(payment);
    }

    @Transactional
    public void handleWebhook(PaymentDto.GimPayByCardResponse data) {
        log.info("Webhook → ref:{} success:{} code:{}",
                data.getMerchantReference(), data.getSuccess(), data.getActionCode());

        // Ignorer webhook intermédiaire 3DS (code 99)
        if (Boolean.FALSE.equals(data.getSuccess()) && "99".equals(data.getActionCode())) {
            log.info("Webhook 3DS intermédiaire ignoré (code 99)");
            return;
        }

        // ── Cas PayLink : la MerchantReference commence par ORDER_ ──
        // GIM Pay envoie la référence de la commande (pas du paiement)
        if (data.getMerchantReference() != null
                && data.getMerchantReference().startsWith("ORDER_")) {
            handlePayLinkWebhook(data);
            return;
        }

        // ── Cas PayByCard : la MerchantReference commence par PAY_ ──
        paymentRepository.findByMerchantReference(data.getMerchantReference()).ifPresent(payment -> {
            Order o = payment.getOrder();
            if (Boolean.TRUE.equals(data.getSuccess())) {
                payment.setStatus(PaymentStatus.SUCCESS);
                o.setStatus(OrderStatus.PAID);
                log.info("✅ Webhook PAID → commande #{}", o.getId());
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                o.setStatus(OrderStatus.FAILED);
                log.warn("❌ Webhook FAILED → commande #{} code:{}", o.getId(), data.getActionCode());
            }
            payment.setActionCode(data.getActionCode());
            orderRepository.save(o);
            paymentRepository.save(payment);
        });
    }

    // ── Traitement PayLink ───────────────────────────────────────────
    // GIM Pay retourne ORDER_XX_xxxx comme MerchantReference pour le PayLink
    private void handlePayLinkWebhook(PaymentDto.GimPayByCardResponse data) {
        log.info("PayLink webhook → ref:{} success:{}", data.getMerchantReference(), data.getSuccess());

        // Trouver la commande via le gimPayOrderRefId ou chercher par ref
        orderRepository.findAll().stream()
                .filter(o -> data.getMerchantReference().equals(
                        "ORDER_" + o.getId() + "_" + (o.getGimPayOrderRefId() != null ? "" : "")
                ) || orderRefMatches(o, data.getMerchantReference()))
                .findFirst()
                .ifPresent(order -> {
                    if (Boolean.TRUE.equals(data.getSuccess())) {
                        order.setStatus(OrderStatus.PAID);
                        orderRepository.save(order);

                        // Créer ou mettre à jour le paiement
                        Optional<Payment> payOpt = paymentRepository.findByOrderId(order.getId());
                        Payment payment = payOpt.orElse(Payment.builder()
                                .order(order).amount(order.getTotalAmount()).build());
                        payment.setStatus(PaymentStatus.SUCCESS);
                        payment.setMethod(PaymentMethod.CARD_3DS_OFF);
                        payment.setMerchantReference(data.getMerchantReference());
                        if (data.getSystemReference() != null)
                            payment.setSystemReference(data.getSystemReference().toString());
                        paymentRepository.save(payment);
                        log.info("✅ PayLink PAID → commande #{}", order.getId());
                    } else {
                        order.setStatus(OrderStatus.FAILED);
                        orderRepository.save(order);
                        log.warn("❌ PayLink FAILED → commande #{}", order.getId());
                    }
                });
    }

    private boolean orderRefMatches(Order order, String ref) {
        // ref format : ORDER_40_0348b5e6
        // Extraire l'ID de commande depuis la ref
        try {
            String[] parts = ref.split("_");
            if (parts.length >= 2) {
                Long orderId = Long.parseLong(parts[1]);
                return orderId.equals(order.getId());
            }
        } catch (Exception ignored) {}
        return false;
    }

    public List<OrderDto.Response> getByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<OrderDto.Response> result = new ArrayList<>();
        for (Order o : orders) result.add(toDto(o));
        return result;
    }

    public OrderDto.Response getById(Long orderId) {
        Order o = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée: " + orderId));
        return toDto(o);
    }

    private OrderDto.Response toDto(Order order) {
        List<OrderDto.ItemResponse> items = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem i : order.getItems()) {
                items.add(OrderDto.ItemResponse.builder()
                        .id(i.getId()).watchId(i.getWatch().getId())
                        .watchName(i.getWatch().getName()).watchBrand(i.getWatch().getBrand())
                        .watchImage(i.getWatch().getImageUrl()).quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice()).subtotal(i.getSubtotal()).build());
            }
        }
        return OrderDto.Response.builder()
                .id(order.getId()).userId(order.getUser().getId())
                .totalAmount(order.getTotalAmount()).status(order.getStatus().name())
                .gimPayOrderId(order.getGimPayOrderId())
                .gimPayOrderUrl(order.getGimPayOrderUrl())
                .gimPayOrderRefId(order.getGimPayOrderRefId())
                .shippingAddress(order.getShippingAddress())
                .createdAt(order.getCreatedAt()).items(items)
                .payment(order.getPayment() != null ? toPaymentDto(order.getPayment()) : null)
                .build();
    }

    private PaymentDto.Response toPaymentDto(Payment p) {
        return PaymentDto.Response.builder()
                .id(p.getId()).amount(p.getAmount()).status(p.getStatus().name())
                .method(p.getMethod() != null ? p.getMethod().name() : null)
                .actionCode(p.getActionCode()).systemReference(p.getSystemReference())
                .merchantReference(p.getMerchantReference())
                .challengeRequired(p.getChallengeRequired())
                .threeDsUrl(p.getThreeDsUrl()).threeDsTxnId(p.getThreeDsTxnId())
                .createdAt(p.getCreatedAt()).build();
    }
}