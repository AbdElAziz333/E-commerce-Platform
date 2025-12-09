package com.aziz.cart_service.service;

import com.aziz.cart_service.dto.AddItemRequest;
import com.aziz.cart_service.dto.CartDto;
import com.aziz.cart_service.dto.CartItemDto;
import com.aziz.cart_service.service.strategy.PersistentCartStrategy;
import com.aziz.cart_service.service.strategy.TemporaryCartStrategy;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final PersistentCartStrategy persistentStrategy;
    private final TemporaryCartStrategy temporaryStrategy;

    private CartStorageStrategy pickStrategy(Long userId) {
        return userId != null ? persistentStrategy : temporaryStrategy;
    }

    public CartDto getOrCreateCart(Long userId, String sessionId) {
        CartStorageStrategy strategy = pickStrategy(userId);
        return strategy.getOrCreateCart(userId, sessionId);
    }

    public int getItemCount(Long userId, String sessionId) {
        CartStorageStrategy strategy = pickStrategy(userId);
        return strategy.getItemCount(userId, sessionId);
    }

    public CartDto addItem(Long userId, String sessionId, AddItemRequest request) {
        CartStorageStrategy strategy = pickStrategy(userId);
        return strategy.addItem(userId, sessionId, request);
    }

    public CartDto updateQuantity(Long userId, String sessionId, String productId, int quantity) {
        CartStorageStrategy strategy = pickStrategy(userId);
        return strategy.updateQuantity(userId, sessionId, productId, quantity);
    }

    public CartDto removeItem(Long userId, String sessionId, String productId) {
        CartStorageStrategy strategy = pickStrategy(userId);
        return strategy.removeItem(userId, sessionId, productId);
    }

    public CartDto mergeGuestCartIntoUserCart(Long userId, String sessionId) {
        if (userId == null) {
            throw new BadRequestException("UserId required to merge guest cart to user cart");
        }

        CartDto guest = temporaryStrategy.getCartIfExists(null, sessionId);
        CartDto userCart = persistentStrategy.getCartIfExists(userId, null);
        Map<String, CartItemDto> map = userCart.getItems().stream()
                .collect(Collectors.toMap(CartItemDto::getProductId, Function.identity()));

        for (CartItemDto gi : guest.getItems()) {
            CartItemDto existing = map.get(gi.getProductId());

            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + gi.getQuantity());
                existing.setTotalPrice(existing.getUnitPrice().multiply(BigDecimal.valueOf(existing.getQuantity())));
            } else {
                CartItemDto toAdd = CartItemDto.builder()
                        .productId(gi.getProductId())
                        .productNameSnapshot(gi.getProductNameSnapshot())
                        .quantity(gi.getQuantity())
                        .unitPrice(gi.getUnitPrice())
                        .totalPrice(gi.getUnitPrice().multiply(BigDecimal.valueOf(gi.getQuantity())))
                        .build();

                userCart.getItems().add(toAdd);
            }
        }

        CartDto saved = persistentStrategy.replaceCart(userId, userCart);
        temporaryStrategy.invalidate(sessionId);
        return saved;
    }
}