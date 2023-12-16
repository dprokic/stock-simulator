package rs.proka.stocksimulator.backtest.api.message;

import rs.proka.stocksimulator.backtest.domain.TradeDirection;
import rs.proka.stocksimulator.backtest.domain.Transaction;

public record TransactionDto(TradeDirection direction, Double instrumentsQuantity, Double instrumentPrice) {
    public static TransactionDto fromTransaction(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return new TransactionDto(transaction.direction(), transaction.instrumentsQuantity(), transaction.instrumentPrice());
    }
}
