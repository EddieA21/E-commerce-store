package com.eddieahiati.ecommerce.controllers;

import com.eddieahiati.ecommerce.dtos.*;
import com.eddieahiati.ecommerce.entities.Cart;
import com.eddieahiati.ecommerce.entities.CartItem;
import com.eddieahiati.ecommerce.mappers.CartItemMapper;
import com.eddieahiati.ecommerce.mappers.CartMapper;
import com.eddieahiati.ecommerce.repositories.CartRepository;
import com.eddieahiati.ecommerce.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;
    private CartItemMapper cartItemMapper;

    @PostMapping
    public ResponseEntity<CartDto> createCart() {
        var cart = new Cart();
        cartRepository.save(cart);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartMapper.cartDto(cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<ProductCartDto> addProductToCart(
            @PathVariable(name = "cartId")String id,
            @RequestBody ProductIdRequest productIdRequest) {

        UUID cartId;

        try {
            cartId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        var productId = productRepository.findById(productIdRequest.getId()).orElse(null);
        if(productId == null) {
            return ResponseEntity.badRequest().build();
        }

        var cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productIdRequest.getId()))
                .findFirst()
                .orElse(null);

        if(cartItem != null)
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        else{
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(productId);
            cartItem.setQuantity(1);
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);

        return ResponseEntity.ok(cartItemMapper.productCartDto(cartItem));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartRequest> getCart(@PathVariable(name = "cartId")String id) {
        UUID cartId;

        try {
            cartId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.cartRequest(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCart(
            @PathVariable(name = "cartId")String id,
            @PathVariable(name = "productId")Long productId,
            @RequestBody UpdateQuantityDto updateQuantityDto
    ) {
        UUID cartId;

        try {
            cartId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));

        var cartItem = cart.getCartItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst().orElse(null);
        if(cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found in cart"));
        }

        cartItem.setQuantity(updateQuantityDto.getQuantity());
        cartRepository.save(cart);

        return ResponseEntity.ok(cartItemMapper.productCartDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeProduct(
            @PathVariable(name = "cartId")String id,
            @PathVariable(name = "productId")Long productId) {
        UUID cartId;

        try {
            cartId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(productId).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }

        var cartItem = cart.getCartItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst().orElse(null);
        cart.getCartItems().remove(cartItem);
        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable(name = "cartId")String id) {
        UUID cartId;

        try {
            cartId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }
}
