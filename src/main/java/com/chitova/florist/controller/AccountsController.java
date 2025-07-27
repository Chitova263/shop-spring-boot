package com.chitova.florist.controller;

import com.chitova.florist.model.account.GenerateAccountManagementTokenOidcRequest;
import com.chitova.florist.model.account.GenerateAccountManagementTokenRequest;
import com.chitova.florist.model.account.RegisterAccountRequest;
import com.chitova.florist.model.account.AccountManagementTokenResponse;
import com.chitova.florist.services.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accounts", description = "Operations for managing accounts")
@RestController
public class AccountsController {

    private final AccountService accountService;

    public AccountsController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
            summary = "Register a new Elastic Path Cloud account",
            description = "Creates a new account in Elastic Path Cloud"
    )
    @PostMapping(value = "/accounts/register", produces = {  MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<AccountManagementTokenResponse> registerAccount(@RequestBody @Valid final RegisterAccountRequest request) {
        return ResponseEntity.ok(accountService.registerAccount(request));
    }

    @Operation(
            summary = "Retrieve an account management token",
            description = "Returns account management token used for account management operations."
    )
    @PostMapping(value = "/accounts/token/password", produces = {  MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<AccountManagementTokenResponse> generateAccountManagementToken(@RequestBody @Valid final GenerateAccountManagementTokenRequest request) {
        return ResponseEntity.ok(accountService.generateAccountManagementToken(request));
    }

    @Operation(
            summary = "Retrieve an account management token",
            description = "Returns account management token used for account management operations."
    )
    @PostMapping(value = "/accounts/token/oidc", produces = {  MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<AccountManagementTokenResponse> generateAccountManagementTokenOidc(@RequestBody @Valid final GenerateAccountManagementTokenOidcRequest request) {
        return ResponseEntity.ok(accountService.generateAccountManagementToken(request));
    }
}
