package com.fabiankaraben.stocksimulator.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Stock(String symbol, BigDecimal price, Instant timestamp) {
}
