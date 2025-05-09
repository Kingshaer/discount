package com.example.discount_module.controller;

import com.example.discount_module.model.DiscountRequest;
import com.example.discount_module.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/discount")

public class DiscountController {
    @Autowired
    private DiscountService discountService;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestBody DiscountRequest request) {
        try {
            double finalPrice = discountService.calculateFinalPrice(request);
            return ResponseEntity.ok(finalPrice);
        } catch (Exception e) {
            e.printStackTrace(); // log เต็มใน console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getClass().getSimpleName(), "message", e.getMessage()));
        }
    }
}

