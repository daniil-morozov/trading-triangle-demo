package com.daniil.triangle.demo.strategy;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.daniil.triangle.demo.indicator.DemoTriangleIndicator;

import java.util.ArrayList;
import java.util.List;

public class DemoTriantleStrategy implements AbstractStrategy<DemoTriangleIndicator> {
    @Override
    public List<DemoTriangleIndicator> detect(List<StockUnit> stocks) {
        List<DemoTriangleIndicator> result = new ArrayList<>();

        StockUnit firstPeak = makeDefaultHigh();
        StockUnit firstDrop = makeDefaultLow();
        StockUnit secondPeak;

        int i = 0;

        while (i < stocks.size()) {
            StockUnit stock = stocks.get(i);
            if (stock.getHigh() > firstPeak.getHigh()) {
                firstPeak = stock;
                firstDrop = makeDefaultLow();
            } else if (stock.getLow() < firstDrop.getLow()) {
                firstDrop = stock;
            } else if (highMinusLow(stock, firstDrop) >=
                    lowestPossible(firstPeak.getHigh() - firstDrop.getLow())
                    && stock.getHigh() < firstPeak.getHigh()) {
                secondPeak = stock;
                int afterSecondPeakIndex = ++i;

                boolean foundSecondDrop = false;

                while (i < stocks.size()) {
                    StockUnit secondDrop = stocks.get(i++);

                    if (highMinusLow(secondPeak, secondDrop) >=
                            lowestPossible(secondPeak.getHigh() - firstDrop.getLow())
                            && secondDrop.getLow() > firstDrop.getLow()) {

                        result.add(new DemoTriangleIndicator(List.of(
                                cloneStock(firstPeak),
                                cloneStock(firstDrop),
                                cloneStock(secondPeak),
                                cloneStock(secondDrop))));
                        firstPeak = makeDefaultHigh();
                        firstDrop = makeDefaultLow();

                        foundSecondDrop = true;

                        break;
                    }
                }

                if (!foundSecondDrop) {
                    i = afterSecondPeakIndex; // retry with higher value
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
