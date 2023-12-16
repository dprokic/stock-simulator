package rs.proka.stocksimulator.backtest.api.message;

import rs.proka.stocksimulator.backtest.domain.StockMarketBacktestResult;

import java.util.List;

public record StockMarketBacktestResultDto(List<BacktestedDayDto> days) {

    public static StockMarketBacktestResultDto fromBacktestResult(StockMarketBacktestResult backtestResult) {
        return new StockMarketBacktestResultDto(backtestResult.days()
                .stream()
                .map(BacktestedDayDto::fromBacktestedDay)
                .toList());
    }

}
