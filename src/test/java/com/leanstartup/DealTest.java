package com.leanstartup;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DealTest {

    private Deal deal;

    @Test
    public void shouldReturnDiscount_forBuyOneGetOneFreeChips() {
        deal = new Deal(100, 2, "Chips");
        assertThat(deal.addItem("Chips"), is(0));
        assertThat(deal.addItem("Chips"), is(100));
        assertThat(deal.addItem("Chips"), is(0));
        assertThat(deal.addItem("Chips"), is(100));
    }

    @Test
    public void shouldReturnDiscount_forMultiItemDiscount_buyThreeGet50pOff() {
        deal = new Deal(50, 3, "Cars", "Planes");
        assertThat(deal.addItem("Ships"), is(0));
        assertThat(deal.addItem("Cars"), is(0));
        assertThat(deal.addItem("Planes"), is(0));
        assertThat(deal.addItem("Planes"), is(50));
    }
}
