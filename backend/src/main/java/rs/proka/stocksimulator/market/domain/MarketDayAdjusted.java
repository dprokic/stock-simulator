package rs.proka.stocksimulator.market.domain;

import java.time.LocalDate;

public record MarketDayAdjusted (MarketDay marketDay, Double adjustedClose) {
    public LocalDate date() {
        return marketDay().getDate();
    }
}
