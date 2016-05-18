package io.github.gengkev.stocksapp;

import java.util.Date;

public class StockPrice {
    Date date;
    long duration;

    // high and low prices
    float high;
    float low;

    // open and close prices
    float open;
    float close;

    // trading volume
    int volume;

    public StockPrice() {
    }
}
