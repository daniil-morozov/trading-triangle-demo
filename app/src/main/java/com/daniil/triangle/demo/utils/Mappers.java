package com.daniil.triangle.demo.utils;


import com.daniil.triangle.demo.dto.Symbol;

public class Mappers {

    private static final String COMMA = ",";

    public static Symbol mapToSymbol(String line) {
        String[] elements = line.split(COMMA);

        return new Symbol(elements[0]);

    }

}
