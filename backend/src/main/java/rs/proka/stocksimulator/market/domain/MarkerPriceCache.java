package rs.proka.stocksimulator.market.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class MarkerPriceCache {

    private Map<Ticker, List<MarketDay>> cache;

    @Value("${stock.cache.maxSize}")
    private Integer maxSize;

    public MarkerPriceCache() {
        cache = new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Ticker, List<MarketDay>> eldest) {
                return size() > maxSize;
            }
        };
    }

    public boolean hasTicker(Ticker ticker) {
        return cache.containsKey(ticker);
    }

    public List<MarketDay> get(Ticker ticker) {
        return cache.get(ticker);
    }

    public void put(Ticker ticker, List<MarketDay> marketDays) {
        cache.put(ticker, marketDays);
    }
}
