package com.chitova.florist.services.sync;

import com.chitova.florist.outbound.products.response.ElasticPathProductPricesResponse;

public record CurrencyPrice(String currency, ElasticPathProductPricesResponse.Currency value) {
}
