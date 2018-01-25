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
        assertThat(new Checkout().scan("Apples"), Matchers.equalTo(100));
        assertThat(new Checkout().scan("Cherries"), Matchers.equalTo(75));
        assertThat(new Checkout().scan("Bananas"), Matchers.equalTo(150));
        assertThat(new Checkout().scan("Pommes"), Matchers.equalTo(100));
        assertThat(new Checkout().scan("Mele"), Matchers.equalTo(100));
    }

    @Test
    public void shouldRecordTotalPrice() {
        assertThat(checkout.getTotalPrice(), is(0));

        int applePrice = checkout.scan("Apples");
        assertThat(applePrice, is(100));
        assertThat(checkout.getTotalPrice(), is(100));

        int basketPrice = checkout.scanBasket("Cherries,Bananas");
        assertThat(basketPrice, is(225));
        assertThat(checkout.getTotalPrice(), is(325));
    }

    @Test
    public void applyCherriesDiscount() {
        int firstTimePrice = checkout.scan("Cherries");
        int priceWithDiscount = checkout.scan("Cherries");

        assertThat(firstTimePrice, is(75));
        assertThat(priceWithDiscount, is(55));
    }

    @Test
    public void applyPommesDiscount() {
        int firstTimePrice = checkout.scan("Pommes");
        int secondTimePrice = checkout.scan("Pommes");
        int thirdTimePriceWithDiscount = checkout.scan("Pommes");

        assertThat(firstTimePrice, is(100));
        assertThat(secondTimePrice, is(100));
        assertThat(thirdTimePriceWithDiscount, is(0));
    }

    @Test
    public void applyMeleDiscount() {
        int firstTimePrice = checkout.scan("Mele");
        int secondTimePriceWithDiscount = checkout.scan("Mele");

        assertThat(firstTimePrice, is(100));
        assertThat(secondTimePriceWithDiscount, is(50));
    }

    @Test
    public void acceptCsv() {
        int price = checkout.scanBasket("Cherries,Cherries");
        assertThat(price, is(130));
    }

    @Test
    public void acceptApplesCsv() {
        int price = checkout.scanBasket("Mele,Pommes,Pommes,Mele");
        assertThat(price, is(250));
    }

    @Test
    public void applyFiveFruitDiscount() {
        int price = checkout.scanBasket("Mele,Pommes,Pommes,Mele,Apples");
        assertThat(price, is(150));
    }

    @Test
    public void calculatePriceForCsv_withMultipleActiveDiscounts() {
        String basket = "Mele,Pommes,Pommes,Apples,Pommes,Mele,Cherries,Cherries,Bananas";
        int price = checkout.scanBasket(basket);
        assertThat(price, is(430));
    }

}