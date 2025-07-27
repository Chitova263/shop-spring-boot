package com.chitova.florist.controller;

import com.chitova.florist.model.accounts.GenerateAccountManagementTokenRequest;
import com.chitova.florist.model.accounts.RegisterAccountRequest;
import com.chitova.florist.model.accounts.AccountManagementTokenResponse;
import com.chitova.florist.services.accounts.AccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accounts")
@RestController
public class AccountsController {

    private final AccountsService accountsService;

    public AccountsController(final AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @Operation(
            summary = "Register a new Elastic Path Cloud account",
            description = "Creates a new account in Elastic Path Cloud",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account registration successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountManagementTokenResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    )
            }
    )
    @PostMapping("/accounts/register")
    public ResponseEntity<AccountManagementTokenResponse> registerAccount(@RequestBody @Valid final RegisterAccountRequest request) {
        AccountManagementTokenResponse response = accountsService.registerAccount(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Retrieve an account management token",
            description = "Returns account management token used for account management operations.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account management token retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountManagementTokenResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    )
            }
    )
    @PostMapping("/accounts/token")
    public ResponseEntity<AccountManagementTokenResponse> generateAccountManagementToken(@RequestBody @Valid final GenerateAccountManagementTokenRequest request) {
        AccountManagementTokenResponse response = accountsService.generateAccountManagementToken(request);
        return ResponseEntity.ok(response);
    }
}
