package com.example.Assignment3_UI.controller;

import com.example.Assignment3_UI.model.Product;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final RestClient restClient;

    public CartController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://assignment3-api-iwwi.onrender.com").build();
    }

    @GetMapping()
    public String getCart(Model model) {
        var response = restClient.get()
                .uri("/cart")
                .retrieve()
                .body(Product[].class);

        List<Product> cartItems;
        if (response != null) {
            cartItems = List.of(response);
        } else {
            cartItems = List.of();
        }

        double totalCost = 0.0;
        for (Product product : cartItems) {
            totalCost += product.getPrice();
        }
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("cartItems", cartItems);
        return "Cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(Product product) {
        product.setIsActive(true);
        restClient.put()
                .uri("/products/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(product)
                .retrieve()
                .toBodilessEntity();
        return "redirect:/products";
    }

    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(@PathVariable Long id) {
        restClient.delete()
                .uri("/cart/{id}", id)
                .retrieve()
                .toBodilessEntity();
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        restClient.delete()
                .uri("/cart/emptycart")
                .retrieve()
                .toBodilessEntity();
        return "redirect:/cart";
    }
}
