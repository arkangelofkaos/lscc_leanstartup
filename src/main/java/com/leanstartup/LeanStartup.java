package com.leanstartup;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class LeanStartup {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        LeanStartup leanStartup = new LeanStartup();
        int total = 0;

        while (sc.hasNext()) {
            String input = sc.nextLine();
            Integer price;

//            if (input.contains(",")) {
//                price = leanStartup.getBasketPrice(input);
//            } else {
                price = leanStartup.getPrice(input);
//            }

            if (price != null) {
                total += price;
            }
            System.out.println(total);
        }
    }


    private boolean cherriesOrderedBefore = false;


    public int getBasketPrice(String basket) {
        String[] items = basket.split(",");
        int total = 0;
        for (String item : items) {
            total += getPrice(item);
        }
        return total;
    }

    public Integer getPrice(String item) {
        Map<String, Integer> itemPrices = new HashMap<String, Integer>() {{
            put("Apples", 100);
            put("Bananas", 150);
            put("Cherries", 75);
        }};

        int discount = 0;
        if (item.equals("Cherries")) {
            if (cherriesOrderedBefore) {
                discount = 20;
                cherriesOrderedBefore = false;
            } else {
                cherriesOrderedBefore = true;
            }
        }

        int finalDiscount = discount;
        return Optional.ofNullable(itemPrices.get(item))
                .map(price -> price - finalDiscount)
                .orElse(0);
    }
}
