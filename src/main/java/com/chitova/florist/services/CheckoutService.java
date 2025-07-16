package com.chitova.florist.services;

import com.chitova.florist.model.checkout.UpdateCartRequest;
import com.chitova.florist.model.checkout.UpdateCartResponse;
import com.chitova.florist.outbound.accounts.ElasticPathCloudAccountsClient;
import com.chitova.florist.outbound.checkout.ElasticPathCloudCheckoutClient;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

    private final ElasticPathCloudCheckoutClient elasticPathCloudCheckoutClient;
    private final ElasticPathCloudAccountsClient elasticPathCloudAccountsClient;

    public CheckoutService(final ElasticPathCloudCheckoutClient elasticPathCloudCheckoutClient,
                           final ElasticPathCloudAccountsClient elasticPathCloudAccountsClient) {
        this.elasticPathCloudCheckoutClient = elasticPathCloudCheckoutClient;
        this.elasticPathCloudAccountsClient = elasticPathCloudAccountsClient;
    }

    public UpdateCartResponse updateCart(UpdateCartRequest request) {
        return null;
    }
}
