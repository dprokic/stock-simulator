package rs.proka.stocksimulator.backtest.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import rs.proka.stocksimulator.market.domain.MarketDay;
import rs.proka.stocksimulator.market.domain.MarketDayAdjusted;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TestBuyCheapSellExpensiveStrategy {

    @Test
    void testStableMarket() {
        BuyCheapSellExpensiveStrategy algorithm = new BuyCheapSellExpensiveStrategy(1000.0, 0.3, 0.5, 100.0, 100.0);

        List<MarketDayAdjusted> marketDays = buildMarketDays(List.of(100.0, 100.0, 100.0, 100.0, 100.0));

        StockMarketBacktestResult backtestResult = algorithm.backtest(marketDays);

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
        assertThat(backtestResult.days(), contains(hasProperty("remainingBudget", is(900.0)),
                hasProperty("remainingBudget", is(900.0)),
                hasProperty("remainingBudget", is(900.0)),
                hasProperty("remainingBudget", is(900.0)),
                hasProperty("remainingBudget", is(900.0))));
    }

    @Test
    void testBullMarket() {
        BuyCheapSellExpensiveStrategy algorithm = new BuyCheapSellExpensiveStrategy(1000.0, 0.3, 0.5, 100.0, 100.0);

        List<MarketDayAdjusted> marketPricesForTimeInterval = buildMarketDays(List.of(100.0, 149.9, 150.0, 225.0, 337.5));

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
        assertThat(backtestResult.days(), contains(hasProperty("remainingBudget", is(900.0)),
                hasProperty("remainingBudget", is(900.0)),
                hasProperty("remainingBudget", is(800.0)),
                hasProperty("remainingBudget", is(700.0)),
                hasProperty("remainingBudget", is(600.0))));
    }

    @Test
    void testBearMarket() {
        BuyCheapSellExpensiveStrategy algorithm = new BuyCheapSellExpensiveStrategy(1000.0, 0.3, 0.5, 100.0, 100.0);

        List<MarketDayAdjusted> marketPricesForTimeInterval = buildMarketDays(List.of(100.0, 150.0, 105.1, 105.0, 73.5));

        StockMarketBacktestResult backtestResult = algorithm.backtest(marketPricesForTimeInterval);

        assertThat(backtestResult.days().size(), is(5));
        double expectedInstrumentsQuantityDay4 = 1.0 + 1.0 / 1.5 - 1.0 / 1.05;
        assertThat(backtestResult.days(), contains(hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 1.0, 100.0))),
                hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 1.0 / 1.5, 150.0))),
                hasProperty("transaction", nullValue()),
                hasProperty("transaction", is(new Transaction(TradeDirection.SELL, 1.0 / 1.05, 105.0))),
                hasProperty("transaction", is(new Transaction(TradeDirection.SELL, expectedInstrumentsQuantityDay4, 73.5)))));
        assertThat(backtestResult.days(), contains(hasProperty("instrumentsQuantity", is(1.0)),
                hasProperty("instrumentsQuantity", is(1.0 + 1.0 / 1.5)),
                hasProperty("instrumentsQuantity", is(1.0 + 1.0 / 1.5)),
                hasProperty("instrumentsQuantity", is(expectedInstrumentsQuantityDay4)),
                hasProperty("instrumentsQuantity", is(0.0))));
        assertThat(backtestResult.days(), contains(hasProperty("remainingBudget", is(900.0)),
                hasProperty("remainingBudget", is(800.0)),
                hasProperty("remainingBudget", is(800.0)),
                hasProperty("remainingBudget", is(900.0)),
                hasProperty("remainingBudget", is(900.0 + (expectedInstrumentsQuantityDay4) * 73.5))));
    }

    @Test
    void testNotEnoughInitialBudget() {
        BuyCheapSellExpensiveStrategy algorithm = new BuyCheapSellExpensiveStrategy(50.0, 0.3, 0.5, 100.0, 100.0);

        List<MarketDayAdjusted> marketPricesForTimeInterval = buildMarketDays(List.of(100.0));

        StockMarketBacktestResult backtestResult = algorithm.backtest(marketPricesForTimeInterval);

        assertThat(backtestResult.days().size(), is(1));
        assertThat(backtestResult.days(), contains(hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 0.5, 100.0)))));
        assertThat(backtestResult.days(), contains(hasProperty("instrumentsQuantity", is(0.5))));
        assertThat(backtestResult.days(), contains(hasProperty("remainingBudget", is(0.0))));
    }

    @Test
    void testNotEnoughBudgetLater() {
        BuyCheapSellExpensiveStrategy algorithm = new BuyCheapSellExpensiveStrategy(150.0, 0.3, 0.5, 100.0, 100.0);

        List<MarketDayAdjusted> marketPricesForTimeInterval = buildMarketDays(List.of(100.0, 150.0));

        StockMarketBacktestResult backtestResult = algorithm.backtest(marketPricesForTimeInterval);

        assertThat(backtestResult.days().size(), is(2));
        assertThat(backtestResult.days(), contains(hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 1.0, 100.0))),
                hasProperty("transaction", is(new Transaction(TradeDirection.BUY, 50.0 / 150.0, 150.0)))));
        assertThat(backtestResult.days(), contains(hasProperty("instrumentsQuantity", is(1.0)),
                hasProperty("instrumentsQuantity", is(1.0 + 50.0 / 150.0))));
        assertThat(backtestResult.days(), contains(hasProperty("remainingBudget", is(50.0)),
                hasProperty("remainingBudget", is(0.0))));
    }

    private List<MarketDayAdjusted> buildMarketDays(List<Double> marketPrices) {
        List<MarketDayAdjusted> marketPricesForTimeInterval = new LinkedList<>();
        int day = 0;
        for (Double price: marketPrices) {
            marketPricesForTimeInterval.add(buildMarketDay(LocalDate.of(2023, 1, 1).plusDays(day), price));
        }
        return marketPricesForTimeInterval;
    }

    private MarketDayAdjusted buildMarketDay(LocalDate date, double price) {
        return new MarketDayAdjusted(MarketDay.builder()
                .date(date)
                .open(price)
                .low(price)
                .high(price)
                .close(price)
                .volume(10L)
                .build(), price);
    }

}
