package com.edushelf.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edushelf.orderservice.entity.LineItem;

public interface LineItemRepository extends JpaRepository<LineItem, Long> {
}
