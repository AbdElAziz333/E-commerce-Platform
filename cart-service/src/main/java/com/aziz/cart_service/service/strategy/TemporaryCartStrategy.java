package com.aziz.cart_service.service.strategy;

import com.aziz.cart_service.dto.AddItemRequest;
import com.aziz.cart_service.dto.CartDto;
import com.aziz.cart_service.dto.CartItemDto;
import com.aziz.cart_service.mapper.CartMapper;
import com.aziz.cart_service.repository.TemporaryCartRepository;
import com.aziz.cart_service.service.CartStorageStrategy;
import com.aziz.cart_service.util.exceptions.NotFoundException;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemporaryCartStrategy implements CartStorageStrategy {
    private final TemporaryCartRepository repository;
    private final CartMapper mapper;

    @Override
    public CartDto getOrCreateCart(Long userId, String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new BadRequestException("NO session token in TemporaryCartStrategy.getOrCreateCart");
        }

        CartDto cached = repository.getTemporaryCart(sessionId);

        if (cached != null) {
            return cached;
        }

        CartDto dto = CartDto.builder()
                .cartId(null)
                .sessionId(sessionId)
                .userId(null)
                .items(new ArrayList<>())
                .build();

        repository.addTemporaryCart(sessionId, dto);
        return dto;
    }

    @Override
    public CartDto getCartIfExists(Long userId, String sessionId) {
        if (sessionId == null) {
            return null;
        }

        return repository.getTemporaryCart(sessionId);
    }

    @Override
    public int getItemCount(Long userId, String sessionId) {
        CartDto c = getCartIfExists(userId, sessionId);
        return c == null ? 0 : c.getItems().stream().mapToInt(CartItemDto::getQuantity).sum();
    }

    @Override
    public CartDto addItem(Long userId, String sessionId, AddItemRequest request) {
        CartDto cart = getOrCreateCart(null, sessionId);
        Optional<CartItemDto> existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItemDto itemDto = existing.get();
            itemDto.setQuantity(itemDto.getQuantity() + request.getQuantity());
            itemDto.setTotalPrice(itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        } else {
            CartItemDto newItem = mapper.addItemRequestToCartItemDto(request);
            cart.getItems().add(newItem);
        }

        repository.addTemporaryCart(cart.getSessionId(), cart);
        return cart;
    }

    @Override
    public CartDto updateQuantity(Long userId, String sessionId, String productId, int quantity) {
        CartDto cart = getOrCreateCart(null, sessionId);
        CartItemDto itemDto = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst().orElseThrow(() -> new NotFoundException("Item not found"));

        if (quantity <= 0) {
            cart.getItems().remove(quantity);
        } else {
            itemDto.setQuantity(quantity);
            itemDto.setTotalPrice(itemDto.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        repository.addTemporaryCart(sessionId, cart);
        return cart;
    }

    @Override
    public CartDto removeItem(Long userId, String sessionId, String productId) {
        CartDto cart = getOrCreateCart(null, sessionId);
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        repository.addTemporaryCart(sessionId, cart);
        return cart;
    }

    @Override
    public CartDto replaceCart(Long userId, CartDto newCart) {
        repository.addTemporaryCart(newCart.getSessionId(), newCart);
        return newCart;
    }

    @Override
    public void invalidate(String sessionId) {
        repository.invalidateCache(sessionId);
    }
}