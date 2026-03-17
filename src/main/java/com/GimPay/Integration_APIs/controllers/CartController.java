package com.GimPay.Integration_APIs.controllers;

import com.GimPay.Integration_APIs.dtos.CartDto;
import com.GimPay.Integration_APIs.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartDto.Response>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping
    public ResponseEntity<CartDto.Response> addToCart(@RequestParam Long userId,
                                                      @RequestBody CartDto.AddRequest request) {
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CartDto.Response> update(@PathVariable Long itemId,
                                                   @RequestParam Long userId,
                                                   @RequestBody CartDto.UpdateRequest request) {
        CartDto.Response result = cartService.updateQuantity(userId, itemId, request);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId,
                                           @RequestParam Long userId) {
        cartService.removeItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}