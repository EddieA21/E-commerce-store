package com.eddieahiati.ecommerce.controllers;

import com.eddieahiati.ecommerce.dtos.CartIdDto;
import com.eddieahiati.ecommerce.dtos.CheckoutResponse;
import com.eddieahiati.ecommerce.dtos.OrderIdDto;
import com.eddieahiati.ecommerce.entities.Order;
import com.eddieahiati.ecommerce.entities.OrderItem;
import com.eddieahiati.ecommerce.entities.OrderStatus;
import com.eddieahiati.ecommerce.mappers.OrderIdDtoMapper;
import com.eddieahiati.ecommerce.repositories.CartRepository;
import com.eddieahiati.ecommerce.repositories.OrderRepository;
import com.eddieahiati.ecommerce.repositories.UserRepository;
import com.eddieahiati.ecommerce.services.PaystackService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private OrderIdDtoMapper orderIdDtoMapper;
    private UserRepository userRepository;
    private PaystackService paystackservice;

    @PostMapping
    @Transactional
    public ResponseEntity<?> checkout(@RequestBody CartIdDto cartIdDto) {
        var cart = cartRepository.findById(cartIdDto.getId()).orElse(null);
        if(cart == null || cart.getCartItems().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        var user = userRepository.findById(userId).orElseThrow();

        BigDecimal totalPrice = BigDecimal.ZERO;
        Order order = new Order();
        for(var item : cart.getCartItems()) {
            var orderItem = new OrderItem();
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProduct(item.getProduct());
            orderItem.setOrder(order);
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getOrderItems().add(orderItem);
            totalPrice = totalPrice.add(item.getTotalPrice());
        }

        order.setStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        var orderSave = orderRepository.save(order);
        orderSave.setTransactionReference("order_" + orderSave.getId() + "_" + System.currentTimeMillis());
        orderRepository.save(orderSave);
        cart.getCartItems().clear();
        cartRepository.save(cart);

        try {
            String paymentUrl = paystackservice.initializePayment(order);
            return ResponseEntity.ok(new CheckoutResponse(order.getId(), paymentUrl));
        } catch (Exception e) {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            return ResponseEntity.internalServerError().body("Payment initialization failed");
        }
    }
}
