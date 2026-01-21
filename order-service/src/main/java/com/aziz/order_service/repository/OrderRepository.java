package com.aziz.order_service.repository;

import com.aziz.order_service.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByUserId(Long userId, Pageable pageable);
//    Optional<Order> findByIdAndUserId(UUID id, Long userId);
}