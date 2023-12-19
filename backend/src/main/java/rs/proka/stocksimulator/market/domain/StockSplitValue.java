package rs.proka.stocksimulator.market.domain;

import java.time.LocalDate;

public record StockSplitValue (LocalDate date, Double ratio) {
}
