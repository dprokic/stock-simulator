package rs.proka.stocksimulator.market.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.proka.stocksimulator.market.api.message.StockMarketPriceTimeSeriesDto;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Tag(name = "Stock Market")
@RestController
@RequestMapping(path = "${stock.api.v1Path}" + MarketController.PATH, produces = APPLICATION_JSON_VALUE)
public class MarketController {
    public static final String PATH = "/market";

    @GetMapping("/")
    @Operation(summary = "Get market price", description = "Get market price")
    @ApiResponse(responseCode = "200", description = "successful operation",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StockMarketPriceTimeSeriesDto.class ))})
    public StockMarketPriceTimeSeriesDto getMarketPrice() {
        return StockMarketPriceTimeSeriesDto.empty();
    }

}
