package com.daniil.triangle.demo.utils;

import java.io.InputStream;
import java.util.List;

public interface SymbolStreamReader<TSymbol, TStream extends InputStream> {
    List<TSymbol> readAll(TStream stream);
}
