package com.leanstartup;

import java.util.HashMap;
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

    private static final int CHERRY_DISCOUNT = 20;
    private static final int BANANA_DISCOUNT = 150;
    private static final int POMMES_DISCOUNT = 100;
    private static final int MELE_DISCOUNT = 50;

    private int countCherries = 0;
    private int countBananas = 0;
    private boolean meleOrderedBefore = false;
    private int countPommes = 0;
    private int countAllApples = 0;
    private int countAll = 0;

    private int totalPrice = 0;

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
        int discount = 0;
        discount = getCherriesDiscount(item, discount);
        discount = getBananasDiscount(item, discount);
        discount = getPommesDiscount(item, discount);
        discount = getMeleDiscount(item, discount);
        discount = getAllApplesDiscount(item, discount);
        discount = getFiveFruitDiscount(item, discount);

        int finalDiscount = discount;
        int finalPrice = Optional.ofNullable(ITEM_PRICES.get(item))
                .map(price -> price - finalDiscount)
                .orElse(0);
        totalPrice += finalPrice;
        return finalPrice;
    }

    private int getCherriesDiscount(String item, int discount) {
        if (item.equals("Cherries")) {
            countCherries++;
            if (countCherries == 2) {
                countCherries = 0;
                return CHERRY_DISCOUNT;
            }
        }
        return discount;
    }

    private int getBananasDiscount(String item, int discount) {
        if (item.equals("Bananas")) {
            countBananas++;
            if (countBananas == 2) {
                countBananas = 0;
                return BANANA_DISCOUNT;
            }
        }
        return discount;
    }

    private int getMeleDiscount(String item, int discount) {
        if (item.equals("Mele")) {
            if (meleOrderedBefore) {
                meleOrderedBefore = false;
                return MELE_DISCOUNT;
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

    private int getAllApplesDiscount(String item, int discount) {
        if (item.equals("Pommes") ||
                item.equals("Mele") ||
                item.equals("Apples")) {
            countAllApples++;
            if (countAllApples == 4) {
                countAllApples = 0;
                return discount + 100;
            }
        }
        return discount;
    }

    private int getFiveFruitDiscount(String item, int discount) {
        if (item.equals("Pommes") ||
                item.equals("Mele") ||
                item.equals("Apples") ||
                item.equals("Cherries") ||
                item.equals("Bananas")
                ) {
            countAll++;
            if (countAll == 5) {
                countAll = 0;
                return discount + 200;
            }
        }
        return discount;
    }
}
