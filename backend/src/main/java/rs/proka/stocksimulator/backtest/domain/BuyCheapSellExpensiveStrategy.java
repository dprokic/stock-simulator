package rs.proka.stocksimulator.backtest.domain;

import lombok.RequiredArgsConstructor;
import rs.proka.stocksimulator.market.domain.MarketPriceTimeSeriesItem;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class BuyCheapSellExpensiveStrategy {

    private final Double relativeDropTriggeringBuy;
    private final Double relativeJumpTriggeringSell;
    private final Double buyValue;
    private final Double sellValue;

    public StockMarketBacktestResult backtest(List<MarketPriceTimeSeriesItem> marketPricesForTimeInterval) {
        List<BacktestedDay> days = new LinkedList<>();

        Iterator<MarketPriceTimeSeriesItem> marketDaysIterator = marketPricesForTimeInterval.iterator();
        BacktestedDay latestBacktestedDay = getInitialSimulationDay(marketDaysIterator.next());
        days.add(latestBacktestedDay);
        Double latestTransactionPrice = latestBacktestedDay.getMarketDay().getClose();

        while (marketDaysIterator.hasNext()) {
            latestBacktestedDay = getNextSimulationDay(marketDaysIterator.next(), latestBacktestedDay, latestTransactionPrice);
            days.add(latestBacktestedDay);
            if (latestBacktestedDay.transaction() != null) {
                latestTransactionPrice = latestBacktestedDay.marketDay().getClose();
            }
        }

        return new StockMarketBacktestResult(days);
    }

    private BacktestedDay getInitialSimulationDay(MarketPriceTimeSeriesItem initialMarketDay) {
        Double price = initialMarketDay.getClose();
        double quantity = calculateQuantity(buyValue, price);
        return new BacktestedDay(initialMarketDay, new Transaction(TradeDirection.BUY, quantity, price), quantity);
    }

    private BacktestedDay getNextSimulationDay(MarketPriceTimeSeriesItem marketDay, BacktestedDay latestBacktestedDay, Double latestTransactionPrice) {
        Double price = marketDay.getClose();
        if (price >= latestTransactionPrice * (1 + relativeJumpTriggeringSell)) {
            return latestBacktestedDay.buy(calculateQuantity(buyValue, price), marketDay);
        } else if (price <= latestTransactionPrice * (1 - relativeDropTriggeringBuy)) {
            return latestBacktestedDay.sell(calculateQuantity(sellValue, price), marketDay);
        } else {
            return latestBacktestedDay.skip(marketDay);
        }
    }

    private double calculateQuantity(Double value, Double price) {
        return value / price;
    }

}
