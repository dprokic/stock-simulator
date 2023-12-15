package rs.proka.stocksimulator.backtest.domain;

import org.junit.jupiter.api.Test;
import rs.proka.stocksimulator.market.domain.MarketPriceTimeSeriesItem;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TestBuyCheapSellExpensiveStrategy {

    @Test
    void testStableMarket() {
        BuyCheapSellExpensiveStrategy algorithm = new BuyCheapSellExpensiveStrategy(0.3, 0.5, 100.0, 100.0);

        List<MarketPriceTimeSeriesItem> marketPricesForTimeInterval = new LinkedList<>();
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 1), 100.0));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 2), 100.0));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 3), 100.0));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 4), 100.0));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 5), 100.0));

        StockMarketBacktestResult backtestResult = algorithm.backtest(marketPricesForTimeInterval);

        assertThat(backtestResult.days().size(), is(5));
        Transaction expectedFirstTransaction = new Transaction(TradeDirection.BUY, 1.0, 100.0);
        assertThat(backtestResult.days(), contains(hasProperty("transaction", is(expectedFirstTransaction)),
                hasProperty("transaction", nullValue()),
                hasProperty("transaction", nullValue()),
                hasProperty("transaction", nullValue()),
                hasProperty("transaction", nullValue())));
        assertThat(backtestResult.days(), contains(hasProperty("instrumentsQuantity", is(1.0)),
                hasProperty("instrumentsQuantity", is(1.0)),
                hasProperty("instrumentsQuantity", is(1.0)),
                hasProperty("instrumentsQuantity", is(1.0)),
                hasProperty("instrumentsQuantity", is(1.0))));
    }

    @Test
    void testBullMarket() {
        BuyCheapSellExpensiveStrategy algorithm = new BuyCheapSellExpensiveStrategy(0.3, 0.5, 100.0, 100.0);

        List<MarketPriceTimeSeriesItem> marketPricesForTimeInterval = new LinkedList<>();
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 1), 100.0));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 2), 149.9));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 3), 150.0));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 4), 225.0));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 5), 337.5));

        StockMarketBacktestResult backtestResult = algorithm.backtest(marketPricesForTimeInterval);

        assertThat(backtestResult.days().size(), is(5));
        assertThat(backtestResult.days(), contains(hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 1.0, 100.0))),
                hasProperty("transaction", nullValue()),
                hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 1.0 / 1.5, 150.0))),
                hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 1.0 / 2.25, 225.0))),
                hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 1.0 / 3.375, 337.5)))));
        assertThat(backtestResult.days(), contains(hasProperty("instrumentsQuantity", is(1.0)),
                hasProperty("instrumentsQuantity", is(1.0)),
                hasProperty("instrumentsQuantity", is(1.0 + 1.0 / 1.5)),
                hasProperty("instrumentsQuantity", is(1.0 + 1.0 / 1.5 + 1.0 / 2.25)),
                hasProperty("instrumentsQuantity", is(1.0 + 1.0 / 1.5 + 1.0 / 2.25 + 1.0 / 3.375))));
    }

    @Test
    void testBearMarket() {
        BuyCheapSellExpensiveStrategy algorithm = new BuyCheapSellExpensiveStrategy(0.3, 0.5, 100.0, 100.0);

        List<MarketPriceTimeSeriesItem> marketPricesForTimeInterval = new LinkedList<>();
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 1), 100.0));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 2), 150.0));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 3), 105.1));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 4), 105.0));
        marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 5), 73.5));

        StockMarketBacktestResult backtestResult = algorithm.backtest(marketPricesForTimeInterval);

        assertThat(backtestResult.days().size(), is(5));
        assertThat(backtestResult.days(), contains(hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 1.0, 100.0))),
                hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 1.0 / 1.5, 150.0))),
                hasProperty("transaction", nullValue()),
                hasProperty("transaction", is(new Transaction(TradeDirection.SELL, 1.0 / 1.05, 105.0))),
                hasProperty("transaction", is(new Transaction(TradeDirection.SELL, 1.0 + 1.0 / 1.5 - 1.0 / 1.05, 73.5)))));
        assertThat(backtestResult.days(), contains(hasProperty("instrumentsQuantity", is(1.0)),
                hasProperty("instrumentsQuantity", is(1.0 + 1.0 / 1.5)),
                hasProperty("instrumentsQuantity", is(1.0 + 1.0 / 1.5)),
                hasProperty("instrumentsQuantity", is(1.0 + 1.0 / 1.5 - 1.0 / 1.05)),
                hasProperty("instrumentsQuantity", is(0.0))));
    }

    private MarketPriceTimeSeriesItem buildMarketDay(LocalDate date, double price) {
        return MarketPriceTimeSeriesItem.builder()
                .date(date)
                .open(price)
                .low(price)
                .high(price)
                .close(price)
                .volume(10L)
                .build();
    }

}
