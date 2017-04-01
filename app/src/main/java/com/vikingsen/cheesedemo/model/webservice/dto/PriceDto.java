package com.vikingsen.cheesedemo.model.webservice.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDto {

    private long id;
    private long cheeseId;
    private double price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCheeseId() {
        return cheeseId;
    }

    public void setCheeseId(long cheeseId) {
        this.cheeseId = cheeseId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
