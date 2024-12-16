package com.edushelf.orderservice.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cartKey;

    private String customerId;
    private String customerEmail;

    private String anonymousId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LineItem> lineItems = new ArrayList<>();  // Initialize the list

    @Enumerated(EnumType.STRING)
    private CartState cartState = CartState.ACTIVE;  // Default to ACTIVE

    // Default constructor
    public Cart() {
        // Ensure anonymousId is set when customerId is null
        if (this.customerId == null) {
            this.anonymousId = UUID.randomUUID().toString();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCartKey() {
        return cartKey;
    }

    public void setCartKey(String cartKey) {
        this.cartKey = cartKey;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
        // If customerId is null, assign a new anonymousId
        if (this.customerId == null) {
            this.anonymousId = UUID.randomUUID().toString();
        } else {
            this.anonymousId = null;  // Reset anonymousId if customerId is not null
        }
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getAnonymousId() {
        return anonymousId;
    }

    public void setAnonymousId(String anonymousId) {
        this.anonymousId = anonymousId;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public CartState getCartState() {
        return cartState;
    }

    public void setCartState(CartState cartState) {
        this.cartState = cartState;
    }

    // Optional: Uncomment if you want to have a readable string representation of the Cart
    @Override
    public String toString() {
        return "Cart [id=" + id + ", cartKey=" + cartKey + ", customerId=" + customerId + ", customerEmail=" + customerEmail
                + ", anonymousId=" + anonymousId + ", lineItems=" + lineItems + ", cartState=" + cartState + "]";
    }
}
