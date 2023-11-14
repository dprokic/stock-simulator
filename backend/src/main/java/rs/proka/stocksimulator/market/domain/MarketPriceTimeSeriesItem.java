package rs.proka.stocksimulator.market.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class MarketPriceTimeSeriesItem {

    private final LocalDate date;
    private final Double open;
    private final Double high;
    private final Double low;
    private final Double close;
    private final Long volume;

}
