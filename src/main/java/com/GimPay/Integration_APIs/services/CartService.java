package com.GimPay.Integration_APIs.services;

import com.GimPay.Integration_APIs.dtos.CartDto;
import com.GimPay.Integration_APIs.entity.CartItem;
import com.GimPay.Integration_APIs.entity.User;
import com.GimPay.Integration_APIs.entity.Watch;
import com.GimPay.Integration_APIs.repository.CartItemRepository;
import com.GimPay.Integration_APIs.repository.UserRepository;
import com.GimPay.Integration_APIs.repository.WatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final WatchRepository watchRepository;

    public CartService(CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       WatchRepository watchRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.watchRepository = watchRepository;
    }

    public List<CartDto.Response> getCart(Long userId) {
        return cartItemRepository.findByUserId(userId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public CartDto.Response addToCart(Long userId, CartDto.AddRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + userId));
        Watch watch = watchRepository.findById(req.getWatchId())
                .orElseThrow(() -> new RuntimeException("Montre non trouvée: " + req.getWatchId()));

        if (watch.getStock() < req.getQuantity()) {
            throw new RuntimeException("Stock insuffisant. Disponible: " + watch.getStock());
        }

        Optional<CartItem> existing = cartItemRepository.findByUserIdAndWatchId(userId, watch.getId());
        CartItem item;
        if (existing.isPresent()) {
            item = existing.get();
            item.setQuantity(item.getQuantity() + req.getQuantity());
        } else {
            item = CartItem.builder().user(user).watch(watch).quantity(req.getQuantity()).build();
        }
        return toDto(cartItemRepository.save(item));
    }

    @Transactional
    public CartDto.Response updateQuantity(Long userId, Long itemId, CartDto.UpdateRequest req) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé: " + itemId));
        if (!item.getUser().getId().equals(userId)) {
            throw new RuntimeException("Non autorisé");
        }
        if (req.getQuantity() <= 0) {
            cartItemRepository.delete(item);
            return null;
        }
        item.setQuantity(req.getQuantity());
        return toDto(cartItemRepository.save(item));
    }

    @Transactional
    public void removeItem(Long userId, Long itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé: " + itemId));
        if (!item.getUser().getId().equals(userId)) {
            throw new RuntimeException("Non autorisé");
        }
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    private CartDto.Response toDto(CartItem item) {
        BigDecimal subtotal = item.getWatch().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));
        return CartDto.Response.builder()
                .id(item.getId())
                .watchId(item.getWatch().getId())
                .watchName(item.getWatch().getName())
                .watchBrand(item.getWatch().getBrand())
                .watchImage(item.getWatch().getImageUrl())
                .unitPrice(item.getWatch().getPrice())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .build();
    }
}