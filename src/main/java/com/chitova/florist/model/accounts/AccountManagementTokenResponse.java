package com.chitova.florist.model.accounts;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountManagementTokenResponse {
    private String accountMemberId;
    private String accountId;
    private String accountName;
    private String token;
    private String tokenType;
    private String tokenExpiry;
    private boolean oneTimePasswordToken;
}
