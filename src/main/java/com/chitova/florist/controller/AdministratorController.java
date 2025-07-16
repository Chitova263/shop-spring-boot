package com.chitova.florist.controller;

import com.chitova.florist.services.AccountSettingsSyncService;
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
    private final AccountSettingsSyncService accountSettingsSyncService;

    public AdministratorController(final ProductSyncService productSyncService, final AccountSettingsSyncService accountSettingsSyncService) {
        this.productSyncService = productSyncService;
        this.accountSettingsSyncService = accountSettingsSyncService;
    }

    @PostMapping("/synchronize/catalog")
    public ResponseEntity syncProducts() {
        productSyncService.synchronize();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/synchronize/accounts")
    public ResponseEntity syncAccounts() {
        accountSettingsSyncService.syncAccountSettings();
        return ResponseEntity.noContent().build();
    }
}
