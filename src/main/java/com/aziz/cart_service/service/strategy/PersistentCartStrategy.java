package com.aziz.cart_service.service.strategy;

import com.aziz.cart_service.dto.AddItemRequest;
import com.aziz.cart_service.dto.CartDto;
import com.aziz.cart_service.dto.CartItemDto;
import com.aziz.cart_service.mapper.CartMapper;
import com.aziz.cart_service.model.Cart;
import com.aziz.cart_service.model.CartItem;
import com.aziz.cart_service.repository.CartRepository;
import com.aziz.cart_service.service.CartStorageStrategy;
import com.aziz.cart_service.util.CartStatus;
import com.aziz.cart_service.util.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersistentCartStrategy implements CartStorageStrategy {
    private final CartRepository cartRepository;
    private final CartMapper mapper;

    @Override
    @Transactional
    public CartDto getOrCreateCart(Long userId, String sessionId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId required for persistent cart storage.");
        }

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> createNewCart(userId));

        return mapper.cartToDto(cart);
    }

    @Override
    @Transactional
    public CartDto getCartIfExists(Long userId, String sessionId) {
        if (userId == null) {
            return null;
        }

        return cartRepository.findByUserId(userId).map(mapper::cartToDto).orElse(null);
    }

    @Override
    @Transactional
    public int getItemCount(Long userId, String sessionId) {
        return cartRepository.findByUserId(userId).map(cart -> cart.getItems().stream()
                .mapToInt(CartItem::getQuantity).sum()).orElse(0);
    }

    @Override
    @Transactional
    public CartDto addItem(Long userId, String sessionId, AddItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> createNewCart(userId));
        Optional<CartItem> existing = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem it = existing.get();
            it.setQuantity(request.getQuantity());
            it.setTotalPrice(request.getUnitPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
        } else {
            CartItem cartItem = mapper.addItemRequestToCartItem(request);
            cartItem.setCart(cart);

            cart.getItems().add(cartItem);
        }

        Cart saved = cartRepository.save(cart);
        return mapper.cartToDto(saved);
    }

    @Override
    @Transactional
    public CartDto updateQuantity(Long userId, String sessionId, String productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        CartItem item = cart.getItems().stream().filter(i -> i.getProductId().equals(productId))
                .findFirst().orElseThrow(() -> new NotFoundException("Item not found"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
            item.setTotalPrice(item.getUnitPriceSnapshot().multiply(BigDecimal.valueOf(quantity)));
        }

        Cart saved = cartRepository.save(cart);
        return mapper.cartToDto(saved);
    }

    @Override
    @Transactional
    public CartDto removeItem(Long userId, String sessionId, String productId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        Cart saved = cartRepository.save(cart);
        return mapper.cartToDto(saved);
    }

    @Override
    @Transactional
    public CartDto replaceCart(Long userId, CartDto newCart) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> createNewCart(userId));
        cart.getItems().clear();

        for (CartItemDto itemDto : newCart.getItems()) {
            CartItem ci = mapper.cartItemDtoToCartItem(itemDto);
            ci.setCart(cart);

            cart.getItems().add(ci);
        }

        Cart saved = cartRepository.save(cart);
        return mapper.cartToDto(saved);
    }

    @Override
    public void invalidate(String sessionId) {
        // nothing to do for persistent strategy ;-;
    }

    public Cart createNewCart(Long userId) {
        Cart cart = Cart.builder()
                .userId(userId)
                .status(CartStatus.ACTIVE)
                .items(new ArrayList<>())
                .build();

        return cartRepository.save(cart);
    }
}