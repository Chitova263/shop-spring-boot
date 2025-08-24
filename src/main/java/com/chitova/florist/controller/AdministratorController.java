package com.chitova.florist.controller;

import com.chitova.florist.services.AccountSettingsSyncService;
import com.chitova.florist.services.seeder.SeederService;
import com.chitova.florist.services.sync.ProductSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Admin")
@RequestMapping("/admin")
@RestController
class AdministratorController {
    private final ProductSyncService productSyncService;
    private final AccountSettingsSyncService accountSettingsSyncService;
    private final SeederService seederService;

    public AdministratorController(final ProductSyncService productSyncService,
                                   final AccountSettingsSyncService accountSettingsSyncService,
                                   final SeederService seederService) {
        this.productSyncService = productSyncService;
        this.accountSettingsSyncService = accountSettingsSyncService;
        this.seederService = seederService;
    }

    @Operation()
    @PostMapping("/synchronize/catalog")
    public ResponseEntity syncProducts() {
        productSyncService.updateCatalog();
        return ResponseEntity.noContent().build();
    }

    @Operation()
    @PostMapping("/synchronize/seed")
    public ResponseEntity seed() throws IOException {
        seederService.seedCatalog();
        return ResponseEntity.noContent().build();
    }

    @Operation()
    @PostMapping("/synchronize/accounts")
    public ResponseEntity syncAccounts() {
        accountSettingsSyncService.syncAccountSettings();
        return ResponseEntity.noContent().build();
    }
}
