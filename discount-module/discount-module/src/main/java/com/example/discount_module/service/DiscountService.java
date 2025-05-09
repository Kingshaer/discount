package com.example.discount_module.service;

import com.example.discount_module.model.CartItem;
import com.example.discount_module.model.DiscountRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DiscountService {

    public double calculateFinalPrice(DiscountRequest request) {
        List<CartItem> items = request.getCartItems();
        double total = items.stream().mapToDouble(CartItem::getPrice).sum();

        if (request.getCoupon() != null) {
            total = applyCouponDiscount(total, request.getCoupon());
        }

        if (request.getOnTop() != null) {
            total = applyOnTopDiscount(total, items, request.getOnTop());
        }

        if (request.getSeasonal() != null) {
            total = applySeasonalDiscount(total, request.getSeasonal());
        }

        return total;
    }

    private double applyOnTopDiscount(double total, List<CartItem> items, Map<String, Object> onTop) {
        String type = (String) onTop.get("type");
        if ("CATEGORY_PERCENT".equalsIgnoreCase(type)) {
            String category = (String) onTop.get("category");
            double percent = (double) onTop.get("percentage");
            double categoryTotal = items.stream()
                    .filter(i -> i.getCategory().equalsIgnoreCase(category))
                    .mapToDouble(CartItem::getPrice).sum();
            return total - (categoryTotal * percent / 100);
        } else if ("POINTS".equalsIgnoreCase(type)) {
            int points = (int) onTop.get("points");
            double maxDiscount = total * 0.2;
            return total - Math.min(points, maxDiscount);
        }
        return total;
    }

    private double applySeasonalDiscount(double total, Map<String, Object> seasonal) {
        int every = (int) seasonal.get("every");
        int discount = (int) seasonal.get("discount");
        return total - ((int)(total / every) * discount);
    }
    private double applyCouponDiscount(double total, Map<String, Object> coupon) {
        String type = (String) coupon.get("type");
        if ("FIXED".equalsIgnoreCase(type)) {
            double amount = getDouble(coupon.get("amount"));
            return total - amount;
        } else if ("PERCENTAGE".equalsIgnoreCase(type)) {
            double percent = getDouble(coupon.get("percentage"));
            return total * (1 - (percent / 100));
        }
        return total;
    }

    private double getDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        } else {
            throw new IllegalArgumentException("Invalid number type: " + value);
        }
    }

}
