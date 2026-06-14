package com.eddieahiati.ecommerce.controllers;

import com.eddieahiati.ecommerce.dtos.ProductDto;
import com.eddieahiati.ecommerce.entities.Product;
import com.eddieahiati.ecommerce.mappers.ProductMapper;
import com.eddieahiati.ecommerce.repositories.CategoryRepository;
import com.eddieahiati.ecommerce.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> showProducts(@RequestParam(required = false, name = "categoryId") Byte categoryId) {
        List<Product> products;

        if(categoryId != null) {
            products = productRepository.findProductByCategoryId(categoryId);
        }
        else{
            products = productRepository.findAll();
        }

        return products.stream().map(p -> productMapper.productDto(p)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable(name = "id")Long id) {
        var product = productRepository.findById(id).orElse(null);

        if(product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.productDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(category == null) {
            return ResponseEntity.badRequest().build();
        }
        var product = productMapper.product(productDto);
        product.setCategory(category);
        productRepository.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable("id")Long id,
            @RequestBody ProductDto productDto) {
        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(category == null) {
            return ResponseEntity.badRequest().build();
        }

        product.setCategory(category);
        productMapper.update(productDto, product);
        productRepository.save(product);

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id")Long id) {
        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }

        productRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
