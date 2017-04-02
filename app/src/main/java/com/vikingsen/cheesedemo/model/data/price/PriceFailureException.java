package com.vikingsen.cheesedemo.model.data.price;


class PriceFailureException extends Exception {

    PriceFailureException() {

    }

    PriceFailureException(Exception e) {
        super(e);
    }
}
