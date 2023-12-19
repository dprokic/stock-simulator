package rs.proka.stocksimulator.market.domain;

import java.util.Optional;

public interface StockSplitRepository {

    Optional<StockSplit> getAllForTicker(Ticker ticker);

    void save(StockSplit stockSplit);

}
