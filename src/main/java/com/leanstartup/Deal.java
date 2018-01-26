package com.leanstartup;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class Deal {
    private final Predicate<String> itemInDeal;
    private final int activationCount;
    private final int discount;

    private int count = 0;

    public Deal(int discount, int activationCount, String... items) {
        Set<String> itemSet = new HashSet<>(Arrays.asList(items));
        this.itemInDeal = itemSet::contains;
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
