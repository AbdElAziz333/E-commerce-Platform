package com.aziz.order_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long orderItemId;

    @Column(nullable = false, updatable = false)
    private String productNameSnapshot;

    @Column(nullable = false, updatable = false)
    private String skuSnapshot;

    @Column(nullable = false, updatable = false)
    private Integer quantity;

    @Column(nullable = false, updatable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false, updatable = false)
    private BigDecimal taxAmount;

    @Column(nullable = false, updatable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false, updatable = false, columnDefinition = "JSONB")
    private List<String> variantAttributes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    private String productId;
}