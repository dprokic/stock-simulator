package rs.proka.stocksimulator.backtest.domain;

import rs.proka.stocksimulator.market.domain.MarketPriceTimeSeriesItem;

public record BacktestedDay(MarketPriceTimeSeriesItem marketDay, Transaction transaction, Double remainingBudget, Double instrumentsQuantity) {

    public BacktestedDay buy(Double quantity, MarketPriceTimeSeriesItem marketDay) {
        double actualQuantity = Math.min(quantity, remainingBudget / marketDay.getClose());
        double newInstrumentsQuantity = instrumentsQuantity + actualQuantity;
        Transaction newTransaction = new Transaction(TradeDirection.BUY, actualQuantity, marketDay.getClose());
        double newRemainingBudget = remainingBudget - actualQuantity * marketDay.getClose();
        return new BacktestedDay(marketDay, newTransaction, newRemainingBudget, newInstrumentsQuantity);
    }

    public BacktestedDay sell(Double quantity, MarketPriceTimeSeriesItem marketDay) {
        double newInstrumentsQuantity = instrumentsQuantity - quantity;
        if (newInstrumentsQuantity < 0.0) {
            newInstrumentsQuantity = 0.0;
        }
        double quantityToSell = instrumentsQuantity - newInstrumentsQuantity;
        Transaction newTransaction = new Transaction(TradeDirection.SELL, quantityToSell, marketDay.getClose());
        double newRemainingBudget = remainingBudget + quantityToSell * marketDay.getClose();
        return new BacktestedDay(marketDay, newTransaction, newRemainingBudget, newInstrumentsQuantity);
    }

    public BacktestedDay skip(MarketPriceTimeSeriesItem marketDay) {
        return new BacktestedDay(marketDay, null, remainingBudget, instrumentsQuantity);
    }

    // TODO: Find solution to remove getters which are used because hasProperty matcher requires java bean, which records are not

    public MarketPriceTimeSeriesItem getMarketDay() {
        return marketDay;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Double getInstrumentsQuantity() {
        return instrumentsQuantity;
    }

    public Double getRemainingBudget() {
        return remainingBudget;
    }
}
