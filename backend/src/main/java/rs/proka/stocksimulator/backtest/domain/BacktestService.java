package rs.proka.stocksimulator.backtest.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rs.proka.stocksimulator.market.domain.MarketDay;
import rs.proka.stocksimulator.market.domain.MarketService;
import rs.proka.stocksimulator.market.domain.Ticker;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BacktestService {

    private final MarketService marketService;

    public StockMarketBacktestResult getStrategyBacktested(Ticker ticker, LocalDate intervalStartDate, LocalDate intervalEndDate, String strategyName, Map<String, String> customParams) {
        List<MarketDay> marketDays = marketService.getMarketPricesForTimeInterval(ticker, intervalStartDate, intervalEndDate);
        BacktestStrategy strategy = getStrategy(strategyName, customParams);
        return strategy.backtest(marketDays);
    }

    private BacktestStrategy getStrategy(String strategyName, Map<String, String> customParams) {
        if ("BuyCheapSellExpensive".equals(strategyName)) {
            if (!customParams.containsKey("initialBudget")
                    || !customParams.containsKey("relativeDropTriggeringBuy")
                    || !customParams.containsKey("relativeJumpTriggeringSell")
                    || !customParams.containsKey("buyValue")
                    || !customParams.containsKey("sellValue")) {
                throw new IllegalArgumentException("Missing required custom param");
            }
            double initialBudget = Double.parseDouble(customParams.get("initialBudget"));
            double relativeDropTriggeringBuy = Double.parseDouble(customParams.get("relativeDropTriggeringBuy"));
            double relativeJumpTriggeringSell = Double.parseDouble(customParams.get("relativeJumpTriggeringSell"));
            double buyValue = Double.parseDouble(customParams.get("buyValue"));
            double sellValue = Double.parseDouble(customParams.get("sellValue"));
            return new BuyCheapSellExpensiveStrategy(initialBudget, relativeDropTriggeringBuy, relativeJumpTriggeringSell, buyValue, sellValue);
        }
        throw new IllegalArgumentException(String.format("Unknown strategy: %s", strategyName));
    }
}
