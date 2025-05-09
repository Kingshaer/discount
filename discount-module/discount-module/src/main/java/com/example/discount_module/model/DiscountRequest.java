package com.example.discount_module.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DiscountRequest {
    private List<CartItem> cartItems;
    private Map<String, Object> coupon;
    private Map<String, Object> onTop;
    private Map<String, Object> seasonal;
}
