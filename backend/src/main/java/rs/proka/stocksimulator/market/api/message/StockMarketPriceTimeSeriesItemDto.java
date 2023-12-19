package rs.proka.stocksimulator.market.api.message;

import lombok.Builder;
import lombok.Getter;
import rs.proka.stocksimulator.market.domain.MarketDay;
import rs.proka.stocksimulator.market.domain.MarketDayAdjusted;

import java.time.LocalDate;

@Builder
@Getter
public class StockMarketPriceTimeSeriesItemDto {

    private final LocalDate date;
    private final Double open;
    private final Double high;
    private final Double low;
    private final Double close;
    private final Double adjustedClose;
    private final Long volume;

    public static StockMarketPriceTimeSeriesItemDto fromItem(MarketDayAdjusted item) {
        return StockMarketPriceTimeSeriesItemDto.builder()
                .date(item.date())
                .open(item.marketDay().getOpen())
                .high(item.marketDay().getHigh())
                .low(item.marketDay().getLow())
                .close(item.marketDay().getClose())
                .adjustedClose(item.adjustedClose())
                .volume(item.marketDay().getVolume())
                .build();
    }
}
