package rs.proka.stocksimulator.market.domain;

import java.util.Collection;

public record StockSplit(Ticker ticker, Collection<StockSplitValue> splits) {
}
