package com.leanstartup;

import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Checkout checkout = new Checkout();

        while (sc.hasNext()) {
            String item = sc.nextLine();
            Integer price;

            if (item.contains(",")) {
                price = checkout.scanBasket(item);
            } else {
                price = checkout.scan(item);
            }

            if (price != 0) {
                System.out.println(checkout.getTotalPrice());
            }
        }
    }
}
