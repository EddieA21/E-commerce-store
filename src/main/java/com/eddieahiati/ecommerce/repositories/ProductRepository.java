package com.eddieahiati.ecommerce.repositories;

import com.eddieahiati.ecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.category.id = :id")
    List<Product> findProductByCategoryId(@Param("id")Byte id);
}
