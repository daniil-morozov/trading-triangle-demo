package com.daniil.triangle.demo.strategy;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.daniil.triangle.demo.indicator.AbstractIndicator;

import java.util.List;

public interface  AbstractStrategy<Indicator extends AbstractIndicator> {
    List<Indicator> detect(List<StockUnit> stocks);
}
