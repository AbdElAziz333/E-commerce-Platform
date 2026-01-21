package com.aziz.auth_service.controller;

import com.aziz.auth_service.dto.AddressDto;
import com.aziz.auth_service.request.CreateAddressRequest;
import com.aziz.auth_service.request.UpdateAddressRequest;
import com.aziz.auth_service.service.AddressService;
import com.aziz.auth_service.util.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AddressDto>>> getAddresses(
            @RequestHeader("User-Id") Long userId,
            @RequestParam(defaultValue = "0") @Min(0) int page
    ) {
        return ResponseEntity.ok(ApiResponse.success("Addresses fetched successfully", service.getAddresses(userId, page)));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressDto>> getAddressById(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long addressId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Address fetched successfully", service.getAddressById(userId, addressId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDto>> createAddress(
            @RequestHeader("User-Id") Long userId,
            @RequestBody @Valid CreateAddressRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address Created Successfully", service.addAddress(userId, request)));
    }

    @PatchMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long addressId,
            @RequestBody @Valid UpdateAddressRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Address updated successfully",
                        service.updateAddress(userId, addressId, request))
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long addressId
    ) {
        service.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }
}