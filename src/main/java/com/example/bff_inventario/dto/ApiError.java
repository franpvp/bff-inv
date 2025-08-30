package com.example.bff_inventario.dto;

import java.time.OffsetDateTime;

public record ApiError(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(OffsetDateTime.now().toString(), status, error, message, path);
    }
}
