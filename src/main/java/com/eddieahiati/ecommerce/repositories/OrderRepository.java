package com.eddieahiati.ecommerce.repositories;

import com.eddieahiati.ecommerce.entities.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "user")
    @Query("SELECT o FROM Order o WHERE o.user.id = :id")
    List<Order> findOrderFromUserId(@Param("id")Long id);

    @Query("SELECT o FROM Order o WHERE o.transactionReference = :reference")
    Optional<Order> findByTransactionReference(@Param("reference")String reference);
}
