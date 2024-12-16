package com.edushelf.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edushelf.orderservice.entity.Cart;
import com.edushelf.orderservice.entity.CartState;
import com.edushelf.orderservice.service.CartService;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestParam String cartKey, @RequestParam String customerEmail) {
        Cart cart = cartService.createCart(cartKey, customerEmail);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{cartKey}/line-items")
    public ResponseEntity<Cart> addLineItem(@PathVariable String cartKey, @RequestParam String productId, @RequestParam String sku, @RequestParam int quantity) {
        Cart cart = cartService.addLineItem(cartKey, productId, sku, quantity);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartKey}/line-items/{lineItemId}")
    public ResponseEntity<Cart> removeLineItem(@PathVariable String cartKey, @PathVariable Long lineItemId) {
        Cart cart = cartService.removeLineItem(cartKey, lineItemId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{cartKey}/line-items/{lineItemId}")
    public ResponseEntity<Cart> changeLineItemQuantity(@PathVariable String cartKey, @PathVariable Long lineItemId, @RequestParam int quantity) {
        Cart cart = cartService.changeLineItemQuantity(cartKey, lineItemId, quantity);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{cartKey}/state")
    public ResponseEntity<Cart> updateCartState(@PathVariable String cartKey, @RequestParam CartState cartState) {
        Cart cart = cartService.updateCartState(cartKey, cartState);
        return ResponseEntity.ok(cart);
    }
}
