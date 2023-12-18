package rs.proka.stocksimulator.backtest.domain;

import rs.proka.stocksimulator.market.domain.MarketDay;

import java.util.List;

public interface BacktestStrategy {
    StockMarketBacktestResult backtest(List<MarketDay> marketPricesForTimeInterval);
}
