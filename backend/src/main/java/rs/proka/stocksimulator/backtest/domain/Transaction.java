package rs.proka.stocksimulator.backtest.domain;

public record Transaction(TradeDirection direction, Double instrumentsQuantity, Double instrumentPrice) {

    public Double value() {
        if (direction == TradeDirection.BUY) {
            return instrumentPrice * instrumentsQuantity;
        } else {
            return -instrumentPrice * instrumentsQuantity;
        }
    }

}
