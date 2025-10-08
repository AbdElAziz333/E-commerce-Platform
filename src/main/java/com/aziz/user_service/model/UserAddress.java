package com.aziz.user_service.model;

import com.aziz.user_service.util.City;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAddressId;

    @Column(nullable = false)
    private String streetLine1;

    private String streetLine2;

    @Enumerated(EnumType.STRING)
    private City city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
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