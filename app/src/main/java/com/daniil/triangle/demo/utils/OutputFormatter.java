package com.daniil.triangle.demo.utils;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;

public class OutputFormatter {
    public static String beautifyStockUnit(StockUnit stockUnit) {
        return stockUnit.getDate() +
                ", open=" + stockUnit.getOpen() +
                " high=" + stockUnit.getHigh() +
                " low=" + stockUnit.getLow() +
                " close=" + stockUnit.getClose();
    }
}
