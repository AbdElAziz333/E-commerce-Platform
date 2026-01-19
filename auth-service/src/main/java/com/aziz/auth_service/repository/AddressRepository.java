package com.aziz.auth_service.repository;

import com.aziz.auth_service.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Page<Address> findAllByUserId(Long userId, Pageable pageable);
    Optional<Address> findByIdAndUserId(Long id, Long userId);
}