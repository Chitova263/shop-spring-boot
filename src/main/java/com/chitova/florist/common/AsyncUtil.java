package com.chitova.florist.common;

import java.util.concurrent.CompletableFuture;

public class AsyncUtil {
    public static <T> T getAsync(CompletableFuture<T> completableFuture) {
        try {
            return completableFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
