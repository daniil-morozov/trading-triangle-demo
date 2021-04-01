package com.daniil.triangle.demo.strategy;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.daniil.triangle.demo.indicator.DemoTriangleIndicator;

import java.util.ArrayList;
import java.util.List;

public class DemoTriangleStrategyFromLowest implements AbstractStrategy<DemoTriangleIndicator> {
    @Override
    public List<DemoTriangleIndicator> detect(List<StockUnit> stocks) {
        List<DemoTriangleIndicator> result = new ArrayList<>();

        StockUnit firstPeak = makeDefaultHigh();
        StockUnit firstDrop = makeDefaultLow();
        StockUnit secondDrop;

        int i = 0;

        while (i < stocks.size()) {
            StockUnit stock = stocks.get(i);
            if (stock.getLow() < firstDrop.getLow()) {
                firstDrop = stock;
                firstPeak = makeDefaultHigh();
            } else if (stock.getHigh() > firstPeak.getHigh()) {
                firstPeak = stock;
            } else if (highMinusLow(firstPeak, stock) >=
                    lowestPossible(firstPeak.getHigh() - firstDrop.getLow())
                    && stock.getLow() > firstDrop.getLow()) {
                secondDrop = stock;
                int afterSecondDropIndex = ++i;

                boolean foundSecondPeak = false;

                while (i < stocks.size()) {
                    StockUnit secondPeak = stocks.get(i++);

                    if (highMinusLow(secondPeak, secondDrop) >=
                            lowestPossible(firstPeak.getHigh() - secondDrop.getLow())
                            && secondPeak.getHigh() < firstPeak.getHigh()) {

                        result.add(new DemoTriangleIndicator(List.of(
                                cloneStock(firstDrop),
                                cloneStock(firstPeak),
                                cloneStock(secondDrop),
                                cloneStock(secondPeak))));
                        firstPeak = makeDefaultHigh();
                        firstDrop = makeDefaultLow();

                        foundSecondPeak = true;

                        break;
                    }
                }

                if (!foundSecondPeak) {
                    i = afterSecondDropIndex; // retry with higher value
                    continue;
                }
            }
            i++;
        }

        return result;
    }

    private double highMinusLow(StockUnit hi, StockUnit low) {
        return hi.getHigh() - low.getLow();
    }

    private double lowestPossible(double v) {
        return (v) * 0.75;
    }

    private StockUnit cloneStock(StockUnit stockUnit) {
        return new StockUnit.Builder()
                .low(stockUnit.getLow())
                .high(stockUnit.getHigh())
                .adjustedClose(stockUnit.getAdjustedClose())
                .close(stockUnit.getClose())
                .dividendAmount(stockUnit.getDividendAmount())
                .open(stockUnit.getOpen())
                .splitCoefficient(stockUnit.getSplitCoefficient())
                .time(stockUnit.getDate())
                .volume(stockUnit.getVolume())
                .build();
    }

    private StockUnit makeDefaultHigh() {
        return new StockUnit.Builder().high(-1.0).build();
    }

    private StockUnit makeDefaultLow() {
        return new StockUnit.Builder().low(Double.MAX_VALUE).build();
    }
}
