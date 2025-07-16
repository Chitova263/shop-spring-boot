package com.chitova.florist.model.accounts;

import lombok.Data;

@Data
public class GenerateAccountManagementTokenRequest {
    private String email;
    private String password;
    private boolean oneTimePasswordToken;
}
