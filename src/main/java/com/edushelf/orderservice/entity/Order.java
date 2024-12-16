package com.edushelf.orderservice.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String orderNumber;

	@ManyToOne
	private Customer customer;

	private Long cartId;

//	@Colum // Add this line
	private String customerEmail; // Add this line

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Enumerated(EnumType.STRING)
	private PaymentState paymentState;

	@Enumerated(EnumType.STRING)
	private OrderState orderState;

	private LocalDateTime orderDate;

	@OneToMany(cascade = CascadeType.ALL)
	private List<LineItem> lineItems;

	public Order() {
		this.orderDate = LocalDateTime.now();
		this.paymentState = PaymentState.PENDING;
		this.orderState = OrderState.OPEN;
	}

	// Add getter and setter for customerEmail
	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public PaymentState getPaymentState() {
		return paymentState;
	}

	public void setPaymentState(PaymentState paymentState) {
		this.paymentState = paymentState;
	}

	public OrderState getOrderState() {
		return orderState;
	}

	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

//	@Override
//	public String toString() {
//		return "Order [id=" + id + ", orderNumber=" + orderNumber + ", cartId=" + cartId + ", customerEmail="
//				+ customerEmail + ", orderDate=" + orderDate + ", lineItems=" + lineItems + "]";
//	}
}
