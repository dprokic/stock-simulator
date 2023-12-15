package rs.proka.stocksimulator.backtest.domain;

public record Transaction(TradeDirection direction, Double instrumentsQuantity, Double instrumentPrice) {
}
