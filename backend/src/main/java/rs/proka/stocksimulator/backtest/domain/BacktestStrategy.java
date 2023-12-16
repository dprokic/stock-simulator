package rs.proka.stocksimulator.backtest.domain;

import rs.proka.stocksimulator.market.domain.MarketPriceTimeSeriesItem;

import java.util.List;

public interface BacktestStrategy {
    StockMarketBacktestResult backtest(List<MarketPriceTimeSeriesItem> marketPricesForTimeInterval);
}
