package com.daniil.triangle.demo.indicator;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;

import java.util.List;

public class DemoTriangleIndicator implements AbstractIndicator {

    private final List<StockUnit> data;

    public DemoTriangleIndicator(List<StockUnit> data) {
        this.data = data;
    }

    @Override
    public List<StockUnit> getData() {
        return data;
    }
}
