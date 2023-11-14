package rs.proka.stocksimulator.market.api.message;

import lombok.Builder;
import lombok.Getter;

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

}
