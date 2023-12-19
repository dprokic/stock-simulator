package rs.proka.stocksimulator.backtest.api.message;

import rs.proka.stocksimulator.backtest.domain.BacktestedDay;
import rs.proka.stocksimulator.market.api.message.StockMarketPriceTimeSeriesItemDto;

public record BacktestedDayDto(StockMarketPriceTimeSeriesItemDto marketDay, TransactionDto transaction,
                               Double remainingBudget, Double instrumentsQuantity, Double instrumentsValue, Double totalValue) {
    static BacktestedDayDto fromBacktestedDay(BacktestedDay day) {
        if (day == null) {
            return null;
        }
        StockMarketPriceTimeSeriesItemDto marketDay = StockMarketPriceTimeSeriesItemDto.fromItem(day.marketDay());
        TransactionDto transaction = TransactionDto.fromTransaction(day.getTransaction());
        double instrumentsValue = day.getInstrumentsQuantity() * day.marketDay().adjustedClose();
        double totalValue = day.remainingBudget() + instrumentsValue;
        return new BacktestedDayDto(marketDay, transaction, day.remainingBudget(), day.getInstrumentsQuantity(),
                instrumentsValue, totalValue);
    }
}
