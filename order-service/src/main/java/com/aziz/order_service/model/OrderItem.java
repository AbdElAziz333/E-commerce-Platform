package com.aziz.order_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String productNameSnapshot;

    @Column(nullable = false, updatable = false)
    private String skuSnapshot;

    @Column(nullable = false, updatable = false)
    private Integer quantity;

    @Column(nullable = false, updatable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false, updatable = false)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}