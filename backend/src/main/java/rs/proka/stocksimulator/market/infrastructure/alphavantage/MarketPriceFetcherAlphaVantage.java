package rs.proka.stocksimulator.market.infrastructure.alphavantage;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rs.proka.stocksimulator.market.domain.MarketPriceFetcher;
import rs.proka.stocksimulator.market.domain.MarketPriceTimeSeriesItem;

import java.time.LocalDate;
import java.util.*;

@Component
public class MarketPriceFetcherAlphaVantage implements MarketPriceFetcher {
    @Override
    public List<MarketPriceTimeSeriesItem> getMarketPriceTimeSeries(String symbol) {
        LinkedList<MarketPriceTimeSeriesItem> transformedItems = new LinkedList<>();

        RestTemplate restTemplate = new RestTemplate();
        String uri = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&outputsize=full&apikey=T8FLSJWB5VWGAMT7", symbol);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<?> result =
                restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class);
        Object body = result.getBody();
        LinkedHashMap<String, Object> bodyAsMap = (LinkedHashMap<String, Object>) body;
        if (bodyAsMap != null && bodyAsMap.containsKey("Time Series (Daily)")) {
            LinkedHashMap<String, LinkedHashMap<String, String>> rawItems = (LinkedHashMap<String, LinkedHashMap<String, String>>) bodyAsMap.get("Time Series (Daily)");
            for (Map.Entry<String, LinkedHashMap<String, String>> itemEntry : rawItems.entrySet()) {
                String rawDate = itemEntry.getKey();
                LocalDate date = LocalDate.parse(rawDate);
                LinkedHashMap<String, String> rawValue = itemEntry.getValue();
                Double open = Double.parseDouble(rawValue.get("1. open"));
                Double high = Double.parseDouble(rawValue.get("2. high"));
                Double low = Double.parseDouble(rawValue.get("3. low"));
                Double close = Double.parseDouble(rawValue.get("4. close"));
                Long volume = Long.parseLong(rawValue.get("5. volume"));
                transformedItems.add(MarketPriceTimeSeriesItem.builder()
                        .date(date)
                        .open(open)
                        .high(high)
                        .low(low)
                        .close(close)
                        .volume(volume)
                        .build());
            }
        }
        return transformedItems;
    }
}
