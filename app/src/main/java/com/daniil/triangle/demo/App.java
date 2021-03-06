/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.daniil.triangle.demo;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.daniil.triangle.demo.dto.Symbol;
import com.daniil.triangle.demo.indicator.DemoTriangleIndicator;
import com.daniil.triangle.demo.strategy.DemoTriangleStrategyFromLowest;
import com.daniil.triangle.demo.strategy.DemoTriantleStrategy;
import com.daniil.triangle.demo.utils.Mappers;
import com.daniil.triangle.demo.utils.SymbolStreamReaderImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.daniil.triangle.demo.utils.OutputFormatter.beautifyStockUnit;

public class App {

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(
                        "nasdaq-listed-symbols_csv.csv");

        List<Symbol> symbols = SymbolStreamReaderImpl
                .ofMapper(Mappers::mapToSymbol)
                .readAll(resourceAsStream)
                .stream()
                .skip(1)
                .filter(s -> !s.getSymbol().trim().isEmpty())
                .collect(Collectors.toList());

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        String startDate = null;
        String endDate = null;

        try {
            System.out.print("Введите дату начала анализа исторических данных (в формате гггг-ММ-дд, например 2020-07-20) -> ");
            startDate = reader.readLine();
            System.out.print("Введите дату окончания анализа исторических данных (в формате гггг-ММ-дд, например 2020-11-30) -> ");
            endDate = reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Работаем...");

        Config cfg = Config.builder()
                .key("YADS86TG6YAFNDC1")
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);

        for (Symbol symbol : symbols) {
            System.out.println("Анализируем " + symbol.getSymbol() + "...");
            TimeSeriesResponse apiResponse = AlphaVantage.api()
                    .timeSeries()
                    .daily()
                    .forSymbol(symbol.getSymbol())
                    .outputSize(OutputSize.FULL)
                    .fetchSync();

            String finalStartDate = startDate;
            String finalEndDate = endDate;

            List<StockUnit> stocksSelected = apiResponse
                    .getStockUnits()
                    .stream()
                    .filter(stockUnit -> Date.valueOf(stockUnit.getDate()).after(Date.valueOf(finalStartDate)))
                    .filter(stockUnit -> Date.valueOf(stockUnit.getDate()).before(Date.valueOf(finalEndDate)))
                    .sorted(Comparator.comparing(StockUnit::getDate, Comparator.comparing(Date::valueOf)))
                    .collect(Collectors.toList());

            DemoTriantleStrategy strategyFirstHi = new DemoTriantleStrategy();
            DemoTriangleStrategyFromLowest strategyFirstLow = new DemoTriangleStrategyFromLowest();
            List<DemoTriangleIndicator> firstHiDetected = strategyFirstHi.detect(stocksSelected);
            List<DemoTriangleIndicator> firstLowDetected = strategyFirstLow.detect(stocksSelected);

            if (firstHiDetected.size() > 0 || firstLowDetected.size() > 0) {
                System.out.println();
                System.out.println("Для символа " + symbol.getSymbol() + " найдены треугольники");
                System.out.println();
            }
        }


    }

    private static void printDetected(List<DemoTriangleIndicator> stocks) {
        for (DemoTriangleIndicator indicator : stocks) {
            List<StockUnit> candles = indicator.getData();

            System.out.println("Первая верхняя точка: " + beautifyStockUnit(candles.get(0)));
            System.out.println("Первая нижняя точка: " + beautifyStockUnit(candles.get(1)));
            System.out.println("Вторая верхняя точка: " + beautifyStockUnit(candles.get(2)));
            System.out.println("Вторая нижняя точка: " + beautifyStockUnit(candles.get(3)));
            System.out.println();
        }
    }

    private static void printFirstLowDetected(List<DemoTriangleIndicator> stocks) {
        for (DemoTriangleIndicator indicator : stocks) {
            List<StockUnit> candles = indicator.getData();

            System.out.println("Первая нижняя точка: " + beautifyStockUnit(candles.get(0)));
            System.out.println("Первая верхняя точка: " + beautifyStockUnit(candles.get(1)));
            System.out.println("Вторая нижняя точка: " + beautifyStockUnit(candles.get(2)));
            System.out.println("Вторая верхняя точка: " + beautifyStockUnit(candles.get(3)));
            System.out.println();
        }
    }

}
