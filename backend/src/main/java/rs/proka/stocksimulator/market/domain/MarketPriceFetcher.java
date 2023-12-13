package rs.proka.stocksimulator.market.domain;

import java.util.List;

public interface MarketPriceFetcher {

    List<MarketPriceTimeSeriesItem> getMarketPriceTimeSeries(Ticker ticker);

}
