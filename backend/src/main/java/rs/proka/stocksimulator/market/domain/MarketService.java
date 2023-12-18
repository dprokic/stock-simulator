package rs.proka.stocksimulator.market.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class MarketService {

    private final MarketPriceFetcher marketPriceFetcher;
    private final MarkerPriceCache markerPriceCache;

    public List<MarketDay> getMarketPriceTimeSeries(Ticker ticker) {
        if (markerPriceCache.hasTicker(ticker)) {
            log.debug("Returning prices from cache for {}", ticker);
            return markerPriceCache.get(ticker);
        }
        log.debug("Fetching price data for {}", ticker);
        List<MarketDay> marketPriceTimeSeries = marketPriceFetcher.getMarketPriceTimeSeries(ticker);
        markerPriceCache.put(ticker, marketPriceTimeSeries);
        return marketPriceTimeSeries;
    }

    public List<MarketDay> getMarketPricesForTimeInterval(Ticker ticker, LocalDate intervalStart, LocalDate intervalEnd) {
        return getMarketPriceTimeSeries(ticker).stream()
                .filter(item -> !item.getDate().isBefore(intervalStart) && !item.getDate().isAfter(intervalEnd))
                .toList();
    }
}
