package rs.proka.stocksimulator.backtest.domain;

import rs.proka.stocksimulator.market.domain.MarketDayAdjusted;

public record BacktestedDay(MarketDayAdjusted marketDay, Transaction transaction, Double remainingBudget, Double instrumentsQuantity) {

    public BacktestedDay buy(Double quantity, MarketDayAdjusted marketDay) {
        double actualQuantity = Math.min(quantity, remainingBudget / marketDay.adjustedClose());
        double newInstrumentsQuantity = instrumentsQuantity + actualQuantity;
        Transaction newTransaction = new Transaction(TradeDirection.BUY, actualQuantity, marketDay.adjustedClose());
        double newRemainingBudget = remainingBudget - actualQuantity * marketDay.adjustedClose();
        return new BacktestedDay(marketDay, newTransaction, newRemainingBudget, newInstrumentsQuantity);
    }

    public BacktestedDay sell(Double quantity, MarketDayAdjusted marketDay) {
        double newInstrumentsQuantity = instrumentsQuantity - quantity;
        if (newInstrumentsQuantity < 0.0) {
            newInstrumentsQuantity = 0.0;
        }
        double quantityToSell = instrumentsQuantity - newInstrumentsQuantity;
        Transaction newTransaction = new Transaction(TradeDirection.SELL, quantityToSell, marketDay.adjustedClose());
        double newRemainingBudget = remainingBudget + quantityToSell * marketDay.adjustedClose();
        return new BacktestedDay(marketDay, newTransaction, newRemainingBudget, newInstrumentsQuantity);
    }

    public BacktestedDay skip(MarketDayAdjusted marketDay) {
        return new BacktestedDay(marketDay, null, remainingBudget, instrumentsQuantity);
    }

    // TODO: Find solution to remove getters which are used because hasProperty matcher requires java bean, which records are not

    public MarketDayAdjusted getMarketDay() {
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
