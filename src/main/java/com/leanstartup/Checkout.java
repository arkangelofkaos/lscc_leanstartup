package com.leanstartup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Checkout {

    private final Map<String, Integer> ITEM_PRICES = new HashMap<String, Integer>() {{
        put("Apples", 100);
        put("Mele", 100);
        put("Pommes", 100);
        put("Bananas", 150);
        put("Cherries", 75);
    }};

    private final List<Deal> deals;
    private int totalPrice = 0;

    public Checkout() {
        this.deals = Arrays.asList(
                new Deal(20, 2, "Cherries"),
                new Deal(150, 2, "Bananas"),
                new Deal(100, 3, "Pommes"),
                new Deal(50, 2, "Mele"),
                new Deal(100, 4, "Pommes", "Mele", "Apples"),
                new Deal(200, 5, "Pommes", "Mele", "Apples", "Cherries", "Bananas")
        );
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int scanBasket(String basketCsv) {
        String[] items = basketCsv.split(",");
        int total = 0;
        for (String item : items) {
            total += scan(item);
        }
        return total;
    }

    public int scan(String item) {
        int finalDiscount = deals.stream()
                .map(deal -> deal.addItem(item))
                .mapToInt(Integer::intValue)
                .sum();
        int finalPrice = Optional.ofNullable(ITEM_PRICES.get(item))
                .map(price -> price - finalDiscount)
                .orElse(0);
        totalPrice += finalPrice;
        return finalPrice;
    }
}
