package com.edushelf.orderservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edushelf.orderservice.entity.Order;
import com.edushelf.orderservice.entity.OrderState;
import com.edushelf.orderservice.entity.PaymentState;
import com.edushelf.orderservice.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrderFromCart(@RequestParam String cartKey, @RequestParam String orderNumber) {
        Order order = orderService.createOrderFromCart(cartKey, orderNumber);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/payment-state")
    public ResponseEntity<Order> updatePaymentState(@PathVariable Long orderId, @RequestParam PaymentState paymentState) {
        Order order = orderService.updatePaymentState(orderId, paymentState);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/order-state")
    public ResponseEntity<Order> updateOrderState(@PathVariable Long orderId, @RequestParam OrderState orderState) {
        Order order = orderService.updateOrderState(orderId, orderState);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable Long customerId) {
        List<Order> orders = orderService.getOrderHistory(customerId);
        return ResponseEntity.ok(orders);
    }
}
