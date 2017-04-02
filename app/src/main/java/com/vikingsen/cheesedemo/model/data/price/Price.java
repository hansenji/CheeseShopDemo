package com.vikingsen.cheesedemo.model.data.price;


public class Price {
    private final long cheeseId;
    private final double price;

    Price(long cheeseId, double price) {
        this.cheeseId = cheeseId;
        this.price = price;
    }

    public long getCheeseId() {
        return cheeseId;
    }

    public double getPrice() {
        return price;
    }
}
