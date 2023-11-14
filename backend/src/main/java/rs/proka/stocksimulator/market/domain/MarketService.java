package rs.proka.stocksimulator.market.domain;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class MarketService {
    public List<MarketPriceTimeSeriesItem> getMarketPriceTimeSeries(String symbol) {
        return Collections.emptyList();
    }
}
