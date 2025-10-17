package com.aziz.user_service.model;

import com.aziz.user_service.util.City;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(nullable = false, length = 150)
    private String streetLine1;

    @Column(length = 150)
    private String streetLine2;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private City city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 20)
    private String postalCode;

    @Column(nullable = false)
    private Boolean isDefaultShipping;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    private LocalDate lastModifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}