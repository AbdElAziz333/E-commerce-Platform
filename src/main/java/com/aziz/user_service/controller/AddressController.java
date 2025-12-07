package com.aziz.user_service.controller;

import com.aziz.user_service.dto.AddressDto;
import com.aziz.user_service.dto.AddressRegisterRequest;
import com.aziz.user_service.service.AddressService;
import com.aziz.user_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressDto>>> getAllAddresses() {
        return ResponseEntity.ok(ApiResponse.success("Fetched all addresses", service.getAllAddresses()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(String.format("Address with id %s fetched successfully", id), service.getAddressById(id)));
    }

    @GetMapping("/u/{userId}")
    public ResponseEntity<ApiResponse<List<AddressDto>>> getAllAddressesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                ApiResponse.success(String.format("All addresses for user with id: %s fetched successfully", userId),
                service.getAllAddressesByUserId(userId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDto>> addAddress(@RequestBody AddressRegisterRequest request, @RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(
                ApiResponse.success("Address Registered Successfully", service.addAddress(request, userId))
        );
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(@RequestBody AddressDto dto) {
        return ResponseEntity.ok(
                ApiResponse.success(String.format("Address with id %s updated successfully", dto.getAddressId()), service.updateAddress(dto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long id) {
        service.deleteAddress(id);
        return ResponseEntity.ok(
                ApiResponse.success(String.format("Address with id: %s deleted successfully", id), null)
        );
    }
}