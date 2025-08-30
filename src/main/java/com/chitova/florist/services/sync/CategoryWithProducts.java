package com.chitova.florist.services.sync;

import com.chitova.florist.outbound.products.response.ElasticPathNodeProductsResponse;

public record CategoryWithProducts(String elasticPathCloudCategoryId, ElasticPathNodeProductsResponse elasticPathNodeProductsResponses) {
}
