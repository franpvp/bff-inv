package com.example.bff_inventario.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
        name = "bodegasGraphQLClient",
        url  = "${azure.functions.bodegas}"
)
public interface BodegaGraphQLClient {
    @PostMapping(value = "/api/graphql/bodegas", consumes = "application/json")
    Map<String, Object> exec(@RequestBody Map<String, Object> body);
}
