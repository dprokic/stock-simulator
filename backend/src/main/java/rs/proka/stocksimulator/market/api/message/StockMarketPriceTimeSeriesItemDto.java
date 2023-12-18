package rs.proka.stocksimulator.market.api.message;

import lombok.Builder;
import lombok.Getter;
import rs.proka.stocksimulator.market.domain.MarketDay;

import java.time.LocalDate;

@Builder
@Getter
public class StockMarketPriceTimeSeriesItemDto {

    private final LocalDate date;
    private final Double open;
    private final Double high;
    private final Double low;
    private final Double close;
    private final Long volume;

    public static StockMarketPriceTimeSeriesItemDto fromItem(MarketDay item) {
        return StockMarketPriceTimeSeriesItemDto.builder()
                .date(item.getDate())
                .open(item.getOpen())
                .high(item.getHigh())
                .low(item.getLow())
                .close(item.getClose())
                .volume(item.getVolume())
                .build();
    }
}
