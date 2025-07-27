package com.chitova.florist.model.accounts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountManagementTokenResponse {
    @Schema(description = "Account member identifier")
    private String accountMemberId;

    @Schema(description = "Account member account identifier")
    private String accountId;

    @Schema(description = "Account member account name")
    private String accountName;

    @Schema(description = "Account management token")
    private String token;

    @Schema(description = "Account management token type")
    private String tokenType;

    @Schema(description = "Account management token expiry date")
    private String tokenExpiry;

    @Schema(description = "True requested token using one time password token")
    private boolean oneTimePasswordToken;
}
