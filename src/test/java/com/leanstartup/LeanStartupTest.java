package com.leanstartup;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class LeanStartupTest {
    LeanStartup leanStartup = new LeanStartup();

    @Test
    public void shouldReturnThePriceForGivenItem() {
        String item = "Apples";
        assertThat(new LeanStartup().getPrice(item), Matchers.equalTo(100));

        assertThat(new LeanStartup().getPrice("Cherries"), Matchers.equalTo(75));
        assertThat(new LeanStartup().getPrice("Bananas"), Matchers.equalTo(150));
        assertThat(new LeanStartup().getPrice("Pommes"), Matchers.equalTo(100));
        assertThat(new LeanStartup().getPrice("Mele"), Matchers.equalTo(100));
    }

    @Test
    public void recordTheStateOfUsersBasket() {
        int firstTimePrice = leanStartup.getPrice("Cherries");
        int priceWithDiscount = leanStartup.getPrice("Cherries");

        assertThat(firstTimePrice, Matchers.is(75));
        assertThat(priceWithDiscount, Matchers.is(55));
    }

    @Test
    public void applyPommesDiscount() {
        int firstTimePrice = leanStartup.getPrice("Pommes");
        int secondTimePrice = leanStartup.getPrice("Pommes");
        int thirdTimePriceWithDiscount = leanStartup.getPrice("Pommes");

        assertThat(firstTimePrice, Matchers.is(100));
        assertThat(secondTimePrice, Matchers.is(100));
        assertThat(thirdTimePriceWithDiscount, Matchers.is(0));
    }

    @Test
    public void applyMeleDiscount() {
        int firstTimePrice = leanStartup.getPrice("Mele");
        int secondTimePriceWithDiscount = leanStartup.getPrice("Mele");

        assertThat(firstTimePrice, Matchers.is(100));
        assertThat(secondTimePriceWithDiscount, Matchers.is(0));
    }

    @Test
    public void acceptCsv() {
        int price = leanStartup.getBasketPrice("Cherries,Cherries");
        assertThat(price, Matchers.is(130));

    }

}