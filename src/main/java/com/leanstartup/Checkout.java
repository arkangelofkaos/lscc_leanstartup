package com.leanstartup;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Checkout {

    private static final int CHERRY_DISCOUNT = 20;
    private static final int BANANA_DISCOUNT = 150;
    private static final int POMMES_DISCOUNT = 100;
    private static final int MELE_DISCOUNT = 50;



    private boolean cherriesOrderedBefore = false;
    private boolean bananasOrderedBefore = false;
    private boolean meleOrderedBefore = false;
    private int totalPrice = 0;
    private int countPommes = 0;
    private int countAllApples = 0;
    private int countAll = 0;

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


        discount = getAllApplesDiscount(item, discount);
        discount = getFiveFruitDiscount(item, discount);


        int finalDiscount = discount;
        int finalPrice = Optional.ofNullable(itemPrices.get(item))
                .map(price -> price - finalDiscount)
                .orElse(0);
        totalPrice += finalPrice;
        return finalPrice;
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
