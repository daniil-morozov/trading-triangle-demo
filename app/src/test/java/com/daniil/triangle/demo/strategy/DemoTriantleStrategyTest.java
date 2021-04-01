package com.daniil.triangle.demo.strategy;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.daniil.triangle.demo.indicator.DemoTriangleIndicator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DemoTriantleStrategyTest {


    @Test
    public void test() {
        List<StockUnit> stocks = new ArrayList<>();

        final double firstHi = 10.0;
        final double firstLo = 5.0;
        final double secondHi = 9.0;
        final double secondLo = 5.1;
        stocks.add(new StockUnit.Builder().high(firstHi).build());
        stocks.add(new StockUnit.Builder().high(9.0).low(7.0).build());
        stocks.add(new StockUnit.Builder().high(8.96).low(6.5).build());
        stocks.add(new StockUnit.Builder().high(9.1).low(6.0).build());
        stocks.add(new StockUnit.Builder().high(7.1).low(firstLo).build());
        stocks.add(new StockUnit.Builder().high(8.1).low(6.0).build());
        stocks.add(new StockUnit.Builder().high(secondHi).low(6.1).build());
        stocks.add(new StockUnit.Builder().high(8).low(secondLo).build());
        stocks.add(new StockUnit.Builder().high(8).low(6.2).build());


        DemoTriantleStrategy strategy = new DemoTriantleStrategy();
        List<DemoTriangleIndicator> detect = strategy.detect(stocks);
        assertEquals(1, detect.size());
        List<StockUnit> results = detect.get(0).getData();
        assertEquals(4, results.size());
        assertEquals(firstHi, results.get(0).getHigh(), 0.0);
        assertEquals(firstLo, results.get(1).getLow(), 0.0);
        assertEquals(secondHi, results.get(2).getHigh(), 0.0);
        assertEquals(secondLo, results.get(3).getLow(), 0.0);
    }

}