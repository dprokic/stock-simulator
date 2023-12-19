package rs.proka.stocksimulator.market.infrastructure;

import org.springframework.stereotype.Component;
import rs.proka.stocksimulator.market.domain.StockSplit;
import rs.proka.stocksimulator.market.domain.StockSplitRepository;
import rs.proka.stocksimulator.market.domain.StockSplitValue;
import rs.proka.stocksimulator.market.domain.Ticker;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class StockSplitInMemoryRepository implements StockSplitRepository {

    private HashMap<Ticker, StockSplit> storage;

    public StockSplitInMemoryRepository() {
        storage = new HashMap<>();
        demoData();
    }

    @Override
    public Optional<StockSplit> getAllForTicker(Ticker ticker) {
        return Optional.ofNullable(storage.get(ticker));
    }

    @Override
    public void save(StockSplit stockSplit) {
        storage.put(stockSplit.ticker(), stockSplit);
    }

    private void demoData() {
        demoDataNvda();
        demoDataMsft();
    }

    private void demoDataMsft() {
        save(new StockSplit(new Ticker("MSFT"), List.of(
                new StockSplitValue(LocalDate.of(2003, 2, 18), 2.0),
                new StockSplitValue(LocalDate.of(1999, 3, 29), 2.0),
                new StockSplitValue(LocalDate.of(1998, 2, 23), 2.0),
                new StockSplitValue(LocalDate.of(1996, 12, 9), 2.0),
                new StockSplitValue(LocalDate.of(1994, 5, 23), 2.0),
                new StockSplitValue(LocalDate.of(1992, 6, 15), 1.5),
                new StockSplitValue(LocalDate.of(1991, 6, 27), 1.5),
                new StockSplitValue(LocalDate.of(1990, 4, 16), 2.0),
                new StockSplitValue(LocalDate.of(1987, 9, 21), 2.0)
        )));
    }

    private void demoDataNvda() {
        save(new StockSplit(new Ticker("NVDA"), List.of(
                new StockSplitValue(LocalDate.of(2021, 7, 20), 4.0),
                new StockSplitValue(LocalDate.of(2007, 9, 11), 1.5),
                new StockSplitValue(LocalDate.of(2006, 4, 7), 2.0),
                new StockSplitValue(LocalDate.of(2001, 9, 12), 2.0),
                new StockSplitValue(LocalDate.of(2000, 6, 27), 2.0)
        )));
    }
}
