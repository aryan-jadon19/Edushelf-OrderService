package com.edushelf.orderservice.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edushelf.orderservice.entity.Cart;
import com.edushelf.orderservice.entity.CartState;
import com.edushelf.orderservice.entity.LineItem;
import com.edushelf.orderservice.exception.ResourceNotFoundException;
import com.edushelf.orderservice.repository.CartRepository;
import com.edushelf.orderservice.repository.LineItemRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private LineItemRepository lineItemRepository;

    @Autowired
    private PriceService priceService;

    public Cart createCart(String cartKey, String customerEmail) {
        Cart cart = new Cart();
        cart.setCartKey(cartKey);
        cart.setCustomerEmail(customerEmail);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart addLineItem(String cartKey, String productId, String sku, int quantity) {
        Cart cart = validateCart(cartKey);

        LineItem lineItem = cart.getLineItems().stream()
                .filter(item -> item.getProductId().equals(productId) && item.getSku().equals(sku))
                .findFirst()
                .orElse(null);

        if (lineItem != null) {
            lineItem.setQuantity(lineItem.getQuantity() + quantity);
        } else {
            BigDecimal price = priceService.getPrice(productId, sku);
            lineItem = new LineItem();
            lineItem.setProductId(productId);
            lineItem.setSku(sku);
            lineItem.setQuantity(quantity);
            lineItem.setPrice(price);
            lineItem.setCart(cart);
            cart.getLineItems().add(lineItem);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeLineItem(String cartKey, Long lineItemId) {
        Cart cart = validateCart(cartKey);

        cart.getLineItems().removeIf(item -> item.getId().equals(lineItemId));
        lineItemRepository.deleteById(lineItemId);

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart changeLineItemQuantity(String cartKey, Long lineItemId, int quantity) {
        Cart cart = validateCart(cartKey);

        LineItem lineItem = cart.getLineItems().stream()
                .filter(item -> item.getId().equals(lineItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Line item not found"));

        lineItem.setQuantity(quantity);

        return cartRepository.save(cart);
    }

    public Cart updateCartState(String cartKey, CartState cartState) {
        Cart cart = validateCart(cartKey);
        cart.setCartState(cartState);
        return cartRepository.save(cart);
    }

    private Cart validateCart(String cartKey) {
        Optional<Cart> optionalCart = cartRepository.findByCartKey(cartKey);
        if (!optionalCart.isPresent()) {
            throw new ResourceNotFoundException("Cart not found");
        }

        Cart cart = optionalCart.get();
        if (cart.getCartState() != CartState.ACTIVE) {
            throw new IllegalStateException("Cannot modify cart in state: " + cart.getCartState());
        }

        return cart;
    }
}
