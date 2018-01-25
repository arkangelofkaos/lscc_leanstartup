package com.leanstartup;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CheckoutTest {
    private Checkout checkout = null;

    @Before
    public void setup() {
        checkout = new Checkout();
    }

    @Test
    public void shouldReturnTheBasePriceForItems() {
        assertThat(new Checkout().getPrice("Apples"), Matchers.equalTo(100));
        assertThat(new Checkout().getPrice("Cherries"), Matchers.equalTo(75));
        assertThat(new Checkout().getPrice("Bananas"), Matchers.equalTo(150));
        assertThat(new Checkout().getPrice("Pommes"), Matchers.equalTo(100));
        assertThat(new Checkout().getPrice("Mele"), Matchers.equalTo(100));
    }

    @Test
    public void applyCherriesDiscount() {
        int firstTimePrice = checkout.getPrice("Cherries");
        int priceWithDiscount = checkout.getPrice("Cherries");

        assertThat(firstTimePrice, is(75));
        assertThat(priceWithDiscount, is(55));
    }

    @Test
    public void applyPommesDiscount() {
        int firstTimePrice = checkout.getPrice("Pommes");
        int secondTimePrice = checkout.getPrice("Pommes");
        int thirdTimePriceWithDiscount = checkout.getPrice("Pommes");

        assertThat(firstTimePrice, is(100));
        assertThat(secondTimePrice, is(100));
        assertThat(thirdTimePriceWithDiscount, is(0));
    }

    @Test
    public void applyMeleDiscount() {
        int firstTimePrice = checkout.getPrice("Mele");
        int secondTimePriceWithDiscount = checkout.getPrice("Mele");

        assertThat(firstTimePrice, is(100));
        assertThat(secondTimePriceWithDiscount, is(50));
    }

    @Test
    public void acceptCsv() {
        int price = checkout.getBasketPrice("Cherries,Cherries");
        assertThat(price, is(130));
    }

    @Test
    public void acceptApplesCsv() {
        int price = checkout.getBasketPrice("Mele,Pommes,Pommes,Mele");
        assertThat(price, is(250));
    }

    @Test
    public void applyFiveFruitDiscount() {
        int price = checkout.getBasketPrice("Mele,Pommes,Pommes,Mele,Apples");
        assertThat(price, is(150));
    }

    @Test
    public void calculatePriceForCsv_withMultipleActiveDiscounts() {
        String basket = "Mele,Pommes,Pommes,Apples,Pommes,Mele,Cherries,Cherries,Bananas";
        int price = checkout.getBasketPrice(basket);
        assertThat(price, is(430));
    }

}