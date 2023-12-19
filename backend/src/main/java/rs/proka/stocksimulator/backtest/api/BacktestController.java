package rs.proka.stocksimulator.backtest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.proka.stocksimulator.backtest.api.message.StockMarketBacktestResultDto;
import rs.proka.stocksimulator.backtest.domain.BacktestService;
import rs.proka.stocksimulator.market.domain.Ticker;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Market Backtesting")
@RestController
@RequestMapping(path = "${stock.api.v1Path}" + BacktestController.PATH, produces = APPLICATION_JSON_VALUE)
public class BacktestController {

    public static final String PATH = "/backtest";

    private final BacktestService backtestService;

    @GetMapping("/")
    @Operation(summary = "Get strategy backtested", description = "Get strategy backtested")
    @ApiResponse(responseCode = "200", description = "successful operation",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StockMarketBacktestResultDto.class ))})
    public StockMarketBacktestResultDto getMarketPrice(@RequestParam String symbol,
                                                       @RequestParam LocalDate intervalStartDate,
                                                       @RequestParam LocalDate intervalEndDate,
                                                       @RequestParam String strategy,
                                                       @RequestParam(required = true) Map<String, String> customParams) {
        return StockMarketBacktestResultDto.fromBacktestResult(backtestService.getStrategyBacktested(new Ticker(symbol), intervalStartDate, intervalEndDate, strategy, customParams));
    }

}
