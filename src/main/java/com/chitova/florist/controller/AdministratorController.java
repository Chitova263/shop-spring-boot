package com.chitova.florist.controller;

import com.chitova.florist.services.ProductSyncService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin")
@RequestMapping("/admin")
@RestController
class AdministratorController {
    private final ProductSyncService productSyncService;

    public AdministratorController(ProductSyncService productSyncService) {
        this.productSyncService = productSyncService;
    }

    @PostMapping("/synchronize/catalog")
    public ResponseEntity syncProducts() throws Exception {
        productSyncService.synchronize();
        return ResponseEntity.noContent().build();
    }
}
