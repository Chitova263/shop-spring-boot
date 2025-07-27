package com.chitova.florist.logging.masking;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MaskingCommon {
    public static final String MASKED_VALUE = "*****";
    public static final String NOT_AVAILABLE = "n/a";
    public static final Set<String> KEYS_TO_MASK_LOWERCASE = (Set) Stream.of("id_token", "Authorization", "password", "passwort", "pwd", "passwd", "oldpassword", "newpassword", "secret", "credential", "otp", "vouchercode", "voucher-code", "vouchercodes", "voucher-codes", "wlankey", "api_key", "client_id", "client_secret", "token", "access_token", "refresh_token", "couponCode", "trustCode", "nts-user-pass", "Set-Cookie", "X-Csrf-Token").map(String::toLowerCase).collect(Collectors.toUnmodifiableSet());

    public static boolean isKeyToMask(final String key) {
        return key != null && KEYS_TO_MASK_LOWERCASE.contains(key.toLowerCase());
    }
}
