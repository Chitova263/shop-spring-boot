package com.chitova.florist.controller;

import com.chitova.florist.model.account.GenerateAccountManagementTokenOidcRequest;
import com.chitova.florist.model.account.GenerateAccountManagementTokenRequest;
import com.chitova.florist.model.account.RegisterAccountRequest;
import com.chitova.florist.model.account.AccountManagementTokenResponse;
import com.chitova.florist.services.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.*;
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

    @Operation(summary = "Register a new Elastic Path Cloud account",
            description = "Creates a new account in Elastic Path Cloud",
            responses = {
                @ApiResponse(responseCode = "500",
                        description = "A technical problem occurred while executing this endpoint",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class),
                                mediaType = MediaType.APPLICATION_JSON_VALUE
                        )
                ),
                @ApiResponse(responseCode = "401",
                        description = "Unauthorized access",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                        )
                )
            }
    )
    @PostMapping("/accounts/register")
    public ResponseEntity<AccountManagementTokenResponse> registerAccount(@RequestBody @Valid final RegisterAccountRequest request) {
        return ResponseEntity.ok(accountService.registerAccount(request));
    }

    @Operation(
            summary = "Retrieve an account management token",
            description = "Returns account management token used for account management operations.",
            responses = {
                    @ApiResponse(responseCode = "500",
                            description = "A technical problem occurred while executing this endpoint",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized access",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    )
            }
    )
    @PostMapping(value = "/accounts/token/password")
    public ResponseEntity<AccountManagementTokenResponse> generateAccountManagementToken(@RequestBody @Valid final GenerateAccountManagementTokenRequest request) {
        return ResponseEntity.ok(accountService.generateAccountManagementToken(request));
    }

    @Operation(
            summary = "Retrieve an account management token",
            description = "Returns account management token used for account management operations.",
            responses = {
                    @ApiResponse(responseCode = "500",
                            description = "A technical problem occurred while executing this endpoint",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized access",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    )
            }
    )
    @PostMapping(value = "/accounts/token/oidc")
    public ResponseEntity<AccountManagementTokenResponse> generateAccountManagementTokenOidc(@RequestBody @Valid final GenerateAccountManagementTokenOidcRequest request) {
        return ResponseEntity.ok(accountService.generateAccountManagementToken(request));
    }
}
