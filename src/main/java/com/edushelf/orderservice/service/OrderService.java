package com.edushelf.orderservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edushelf.orderservice.entity.Cart;
import com.edushelf.orderservice.entity.CartState;
import com.edushelf.orderservice.entity.Customer;
import com.edushelf.orderservice.entity.LineItem;
import com.edushelf.orderservice.entity.Order;
import com.edushelf.orderservice.entity.OrderState;
import com.edushelf.orderservice.entity.PaymentState;
import com.edushelf.orderservice.exception.ResourceNotFoundException;
import com.edushelf.orderservice.repository.CartRepository;
import com.edushelf.orderservice.repository.CustomerRepository;
import com.edushelf.orderservice.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Order createOrderFromCart(String cartKey, String orderNumber) {
        Optional<Cart> optionalCart = cartRepository.findByCartKey(cartKey);
        if (!optionalCart.isPresent()) {
            throw new ResourceNotFoundException("Cart not found");
        }

        Cart cart = optionalCart.get();
        if (cart.getCartState() != CartState.ACTIVE) {
            throw new IllegalStateException("Cannot create order from cart in state: " + cart.getCartState());
        }

        if (orderRepository.findByOrderNumber(orderNumber).isPresent()) {
            throw new IllegalArgumentException("Order number already exists");
        }

        Customer customer = customerRepository.findByEmail(cart.getCustomerEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setCustomer(customer);
        order.setCartId(cart.getId());
        order.setCustomerEmail(cart.getCustomerEmail());
        order.setLineItems(cart.getLineItems().stream()
                .map(item -> {
                    LineItem orderItem = new LineItem();
                    orderItem.setProductId(item.getProductId());
                    orderItem.setSku(item.getSku());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(item.getPrice());
                    return orderItem;
                }).collect(Collectors.toList()));

        cart.setCartState(CartState.ORDERED);
        cartRepository.save(cart);

        return orderRepository.save(order);
    }

    @Transactional
    public Order updatePaymentState(Long orderId, PaymentState paymentState) {
        Order order = validateOrder(orderId);
        order.setPaymentState(paymentState);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderState(Long orderId, OrderState orderState) {
        Order order = validateOrder(orderId);
        order.setOrderState(orderState);
        return orderRepository.save(order);
    }

    public List<Order> getOrderHistory(Long customerId) {
        return orderRepository.findByCustomer_Id(customerId);
    }

    private Order validateOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new ResourceNotFoundException("Order not found");
        }
        return optionalOrder.get();
    }
}
