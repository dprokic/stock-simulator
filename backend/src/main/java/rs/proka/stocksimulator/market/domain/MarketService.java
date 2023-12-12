package rs.proka.stocksimulator.market.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class MarketService {

    private final MarketPriceFetcher marketPriceFetcher;

    public List<MarketPriceTimeSeriesItem> getMarketPriceTimeSeries(String symbol) {
        return marketPriceFetcher.getMarketPriceTimeSeries(symbol);
    }
}
