package rs.proka.stocksimulator.backtest.domain;

import rs.proka.stocksimulator.market.domain.MarketPriceTimeSeriesItem;

public record BacktestedDay(MarketPriceTimeSeriesItem marketDay, Transaction transaction, Double instrumentsQuantity) {

    public BacktestedDay buy(Double quantity, MarketPriceTimeSeriesItem marketDay) {
        Transaction newTransaction = new Transaction(TradeDirection.BUY, quantity, marketDay.getClose());
        return new BacktestedDay(marketDay, newTransaction, instrumentsQuantity + quantity);
    }

    public BacktestedDay sell(Double quantity, MarketPriceTimeSeriesItem marketDay) {
        double newInstrumentsQuantity = instrumentsQuantity - quantity;
        if (newInstrumentsQuantity < 0.0) {
            newInstrumentsQuantity = 0.0;
        }
        double quantityToSell = instrumentsQuantity - newInstrumentsQuantity;
        Transaction newTransaction = null;
        if (quantityToSell > 0.0) {
            newTransaction = new Transaction(TradeDirection.SELL, quantityToSell, marketDay.getClose());
        }
        return new BacktestedDay(marketDay, newTransaction, newInstrumentsQuantity);
    }

    public BacktestedDay skip(MarketPriceTimeSeriesItem marketDay) {
        return new BacktestedDay(marketDay, null, instrumentsQuantity);
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
}
