package rs.proka.stocksimulator.backtest.domain;

import rs.proka.stocksimulator.market.domain.MarketDayAdjusted;

import java.util.List;

public interface BacktestStrategy {
    StockMarketBacktestResult backtest(List<MarketDayAdjusted> marketPricesForTimeInterval);
}
