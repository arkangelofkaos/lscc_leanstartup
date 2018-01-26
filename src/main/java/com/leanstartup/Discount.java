package com.leanstartup;

import java.util.function.Predicate;

public class Discount {
    private final Predicate<String> itemInDeal;
    private final int activationCount;
    private final int discount;

    private int count = 0;

    public Discount(Predicate<String> itemInDeal, int discount, int activationCount) {
        this.itemInDeal = itemInDeal;
        this.discount = discount;
        this.activationCount = activationCount;
    }

    public int addItem(String item) {
        if (itemInDeal.test(item)) {
            count++;
            if (count == activationCount) {
                count = 0;
                return discount;
            }
        }
        return 0;
    }
}
