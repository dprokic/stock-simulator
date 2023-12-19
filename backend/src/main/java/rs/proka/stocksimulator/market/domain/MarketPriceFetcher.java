package rs.proka.stocksimulator.market.domain;

import java.util.List;

public interface MarketPriceFetcher {

    List<MarketDay> getMarketPriceTimeSeries(Ticker ticker);

    List<MarketDayAdjusted> getMarketPriceTimeSeriesAdjusted(Ticker ticker);
}
