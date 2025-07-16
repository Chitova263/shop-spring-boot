package com.chitova.florist.controller;

import com.chitova.florist.model.accounts.GenerateAccountManagementTokenRequest;
import com.chitova.florist.model.accounts.RegisterAccountRequest;
import com.chitova.florist.model.accounts.AccountManagementTokenResponse;
import com.chitova.florist.services.AccountsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accounts")
@RequestMapping("/florist-api")
@RestController
public class AccountsController {

    private final AccountsService accountsService;

    public AccountsController(final AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping("/accounts/register")
    public ResponseEntity<?> registerAccount(@RequestBody final RegisterAccountRequest request) {
        AccountManagementTokenResponse response = accountsService.registerAccount(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accounts/token")
    public ResponseEntity<?> generateAccountManagementToken(@RequestBody final GenerateAccountManagementTokenRequest request) {
        AccountManagementTokenResponse response = accountsService.generateAccountManagementToken(request);
        return ResponseEntity.ok(response);
    }
}
