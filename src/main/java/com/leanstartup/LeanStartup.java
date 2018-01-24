package com.leanstartup;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class LeanStartup {

    private static final int CHERRY_DISCOUNT = 20;
    private static final int BANANA_DISCOUNT = 150;
    public static final int POMMES_DISCOUNT = 100;

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
    private boolean bananasOrderedBefore = false;
    private boolean meleOrderedBefore = false;
    private int countPommes = 0;


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
            put("Mele", 100);
            put("Pommes", 100);
            put("Bananas", 150);
            put("Cherries", 75);
        }};

        int discount = 0;
        discount = getCherriesDiscount(item, discount);
        discount = getBananasDiscount(item, discount);
        discount = getPommesDiscount(item, discount);
        discount = getMeleDiscount(item, discount);

        int finalDiscount = discount;
        return Optional.ofNullable(itemPrices.get(item))
                .map(price -> price - finalDiscount)
                .orElse(0);
    }

    private int getCherriesDiscount(String item, int discount) {
        if (item.equals("Cherries")) {
            if (cherriesOrderedBefore) {
                cherriesOrderedBefore = false;
                return CHERRY_DISCOUNT;
            } else {
                cherriesOrderedBefore = true;
            }
        }
        return discount;
    }

    private int getBananasDiscount(String item, int discount) {
        if (item.equals("Bananas")) {
            if (bananasOrderedBefore) {
                bananasOrderedBefore = false;
                return BANANA_DISCOUNT;
            } else {
                bananasOrderedBefore = true;
            }
        }
        return discount;
    }

    private int getMeleDiscount(String item, int discount) {
        if (item.equals("Mele")) {
            if (meleOrderedBefore) {
                meleOrderedBefore = false;
                return 50;
            } else {
                meleOrderedBefore = true;
            }
        }
        return discount;
    }

    private int getPommesDiscount(String item, int discount) {
        if (item.equals("Pommes")) {
            countPommes++;
            if (countPommes == 3) {
                countPommes = 0;
                return POMMES_DISCOUNT;
            }
        }
        return discount;
    }
}
