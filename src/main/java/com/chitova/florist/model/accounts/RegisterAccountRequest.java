package com.chitova.florist.model.accounts;

import lombok.Data;

@Data
public class RegisterAccountRequest {
    private String email;
    private String password;
    private String name;
    private String type;
    private boolean oneTimePasswordToken;
}
