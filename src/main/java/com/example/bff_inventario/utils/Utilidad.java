package com.example.bff_inventario.utils;

import com.example.bff_inventario.dto.ApiError;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utilidad {

    public static final String BASE_DIR = "/";
    public static final String BASE_DIR_BODEGA = "/api/bodegas";
    public static final String BASE_DIR_PRODUCTOS = "/api/productos";
    public static final String BASE_DIR_INVENTARIOS = "/api/inventarios";
    public static final String BASE_DIR_GRAPHQL_BODEGA = "/api/graphql/bodegas";
    public static final String BASE_DIR_GRAPHQL_PRODUCTOS = "/api/graphql/productos";
    public static final String BASE_DIR_GRAPHQL_INVENTARIOS = "/api/graphql/inventarios";

    public static ResponseEntity<ApiError> mapFeignError(FeignException ex, String path, String genericMessage) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) status = HttpStatus.BAD_GATEWAY;

        String title = switch (status) {
            case BAD_REQUEST -> "Bad Request";
            case UNAUTHORIZED -> "Unauthorized";
            case FORBIDDEN -> "Forbidden";
            case NOT_FOUND -> "Not Found";
            case CONFLICT -> "Conflict";
            case SERVICE_UNAVAILABLE -> "Service Unavailable";
            case GATEWAY_TIMEOUT -> "Gateway Timeout";
            default -> status.is5xxServerError() ? "Upstream Error" : status.getReasonPhrase();
        };

        return ResponseEntity.status(status)
                .body(ApiError.of(status.value(), title, genericMessage, path));
    }

    public static ResponseEntity<Map<String, Object>> badRequest(String path, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", message);
        body.put("path", path);
        return ResponseEntity.badRequest().body(body);
    }

    public static ResponseEntity<Map<String, Object>> notFound(String path, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", message);
        body.put("path", path);
        return ResponseEntity.status(404).body(body);
    }
}
