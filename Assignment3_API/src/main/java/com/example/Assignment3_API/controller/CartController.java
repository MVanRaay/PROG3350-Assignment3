package com.example.Assignment3_API.controller;

import com.example.Assignment3_API.model.Product;
import com.example.Assignment3_API.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {
    private final ProductRepository productRepository;

    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping()
    public List<Product> GetCart(){
        return productRepository.findByIsActiveTrue();
    }

    @DeleteMapping("/emptycart")
    public void EmptyCart() {
        List<Product> activeItems = productRepository.findByIsActiveTrue();

        activeItems.forEach(item -> item.setIsActive(false));

        productRepository.saveAll(activeItems);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id) {
        Optional<Product> productResult = productRepository.findById(id);
        if (productResult.isPresent()) {
            Product product = productResult.get();
            product.setIsActive(false);
            productRepository.save(product);
        }
    }

}