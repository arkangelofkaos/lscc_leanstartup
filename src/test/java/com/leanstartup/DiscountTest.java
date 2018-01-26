package com.leanstartup;

import org.junit.Test;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DiscountTest {

    private Discount discount;

    @Test
    public void shouldReturnDiscount_forBuyOneGetOneFreeChips() {
        discount = new Discount("Chips"::equals, 100, 2);
        assertThat(discount.addItem("Chips"), is(0));
        assertThat(discount.addItem("Chips"), is(100));
        assertThat(discount.addItem("Chips"), is(0));
        assertThat(discount.addItem("Chips"), is(100));
    }

    @Test
    public void shouldReturnDiscount_forMultiItemDiscount_buyThreeGet50pOff() {
        HashSet<String> discountedItem = new HashSet<String>() {{
            add("Cars");
            add("Planes");
        }};
        discount = new Discount(discountedItem::contains, 50, 3);
        assertThat(discount.addItem("Ships"), is(0));
        assertThat(discount.addItem("Cars"), is(0));
        assertThat(discount.addItem("Planes"), is(0));
        assertThat(discount.addItem("Planes"), is(50));
    }
}
