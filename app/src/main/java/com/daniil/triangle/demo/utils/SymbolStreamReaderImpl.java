package com.daniil.triangle.demo.utils;

import com.daniil.triangle.demo.dto.Symbol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Symbol stream reader
 */
public class SymbolStreamReaderImpl implements SymbolStreamReader<Symbol, InputStream> {

    private final Function<String, Symbol> mapper;

    private SymbolStreamReaderImpl(Function<String, Symbol> mapper) {
        this.mapper = mapper;
    }

    /**
     * Create the reader with specified mapper
     * @param mapper mapper
     * @return instance
     */
    public static SymbolStreamReaderImpl ofMapper(Function<String, Symbol> mapper) {
        return new SymbolStreamReaderImpl(mapper);
    }

    @Override
    public List<Symbol> readAll(InputStream stream) {
        Objects.requireNonNull(stream);
        List<Symbol> points = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            points = reader.lines().map(mapper)
                .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e);
        }

        return points;
    }
}
