package rs.proka.stocksimulator.market.api.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rs.proka.stocksimulator.market.domain.MarketDayAdjusted;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StockMarketPriceTimeSeriesDto {

    private final List<StockMarketPriceTimeSeriesItemDto> timeSeries;

    public static StockMarketPriceTimeSeriesDto fromMarketPriceTimeSeries(List<MarketDayAdjusted> items) {
        return new StockMarketPriceTimeSeriesDto(items.stream()
                .map(StockMarketPriceTimeSeriesItemDto::fromItem)
                .toList());
    }
}
