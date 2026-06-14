package com.eddieahiati.ecommerce.controllers;

import com.eddieahiati.ecommerce.dtos.OrderRequestDto;
import com.eddieahiati.ecommerce.mappers.OrderRequestMapper;
import com.eddieahiati.ecommerce.repositories.OrderRepository;
import com.eddieahiati.ecommerce.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private OrderRequestMapper orderRequestMapper;

    @GetMapping
    public List<OrderRequestDto> getAllOrders() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        var orders = orderRepository.findOrderFromUserId(userId);

        return orders.stream().map(order -> orderRequestMapper.toOrderDto(order)).toList();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRequestDto> getSingleOrder(@PathVariable(name = "orderId")Long id) {
        var order = orderRepository.findById(id).orElse(null);
        if(order == null) {
            return ResponseEntity.notFound().build();
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        if(!order.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(orderRequestMapper.toOrderDto(order));
    }
}
