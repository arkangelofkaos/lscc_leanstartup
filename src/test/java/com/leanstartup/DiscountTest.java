package com.leanstartup;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DiscountTest {

    private Discount discount;

    @Test
    public void shouldReturnBasePrice_whenDiscountConditionNotPassed() {
        discount = new Discount("Chips", 100);
        assertThat(discount.addItem("Chips"), is(100));
    }
}
