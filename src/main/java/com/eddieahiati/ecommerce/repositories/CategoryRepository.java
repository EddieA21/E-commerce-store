package com.eddieahiati.ecommerce.repositories;

import com.eddieahiati.ecommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Byte> {
}
