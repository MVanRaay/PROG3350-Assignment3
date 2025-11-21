package com.example.Assignment3_UI.controller;

import com.example.Assignment3_UI.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Stream;

@Controller
@RequestMapping("/products")
public class ProductsController {

    private final RestClient restClient;

    public ProductsController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://assignment3-api-iwwi.onrender.com").build();
    }

    @GetMapping()
    public String getProducts(Model model) {
        var response = restClient.get()
                .uri("/products")
                .retrieve()
                .body(Product[].class);

        List<Product> products = response == null
                ? List.of()
                : Arrays.stream(response)
                    .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();

        model.addAttribute("products", products);
        return "Products";
    }
}
