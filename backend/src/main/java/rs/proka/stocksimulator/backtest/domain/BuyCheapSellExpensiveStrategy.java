package rs.proka.stocksimulator.backtest.domain;

import lombok.RequiredArgsConstructor;
import rs.proka.stocksimulator.market.domain.MarketDayAdjusted;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class BuyCheapSellExpensiveStrategy implements BacktestStrategy {

    private final Double initialBudget;
    private final Double relativeDropTriggeringBuy;
    private final Double relativeJumpTriggeringSell;
    private final Double buyValue;
    private final Double sellValue;

    @Override
    public StockMarketBacktestResult backtest(List<MarketDayAdjusted> marketDays) {
        List<BacktestedDay> backtestedDays = new LinkedList<>();

        Iterator<MarketDayAdjusted> marketDaysIterator = marketDays.iterator();
        BacktestedDay latestBacktestedDay = getInitialBacktestedDay(initialBudget, marketDaysIterator.next());
        backtestedDays.add(latestBacktestedDay);
        Double latestTransactionPrice = latestBacktestedDay.getMarketDay().adjustedClose();

        while (marketDaysIterator.hasNext()) {
            latestBacktestedDay = getNextBacktestedDay(marketDaysIterator.next(), latestBacktestedDay, latestTransactionPrice);
            backtestedDays.add(latestBacktestedDay);
            if (latestBacktestedDay.transaction() != null) {
                latestTransactionPrice = latestBacktestedDay.marketDay().adjustedClose();
            }
        }

        return new StockMarketBacktestResult(backtestedDays);
    }

    private BacktestedDay getInitialBacktestedDay(Double initialBudget, MarketDayAdjusted initialMarketDay) {
        Double price = initialMarketDay.adjustedClose();
        double quantity = calculateQuantity(Math.min(buyValue, initialBudget), price);
        Double remainingBudget = initialBudget - quantity * price;
        return new BacktestedDay(initialMarketDay, new Transaction(TradeDirection.BUY, quantity, price), remainingBudget, quantity);
    }

    private BacktestedDay getNextBacktestedDay(MarketDayAdjusted marketDay, BacktestedDay latestBacktestedDay, Double latestTransactionPrice) {
        Double price = marketDay.adjustedClose();
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
