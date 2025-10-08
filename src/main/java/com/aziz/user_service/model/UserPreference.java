package com.aziz.user_service.model;

import com.aziz.user_service.util.PreferredCurrency;
import com.aziz.user_service.util.PreferredLanguage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPreferenceId;

    @Enumerated(EnumType.STRING)
    private PreferredLanguage preferredLanguage;

    @Enumerated(EnumType.STRING)
    private PreferredCurrency preferredCurrency;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}