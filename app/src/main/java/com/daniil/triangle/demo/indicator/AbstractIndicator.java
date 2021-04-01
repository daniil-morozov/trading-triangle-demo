package com.daniil.triangle.demo.indicator;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;

import java.util.List;

public interface AbstractIndicator {
    List<StockUnit> getData();
}
