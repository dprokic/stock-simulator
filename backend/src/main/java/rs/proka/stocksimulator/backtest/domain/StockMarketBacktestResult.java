package rs.proka.stocksimulator.backtest.domain;

import java.util.List;

public record StockMarketBacktestResult(List<BacktestedDay> days) {
}
