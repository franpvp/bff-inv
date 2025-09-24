package com.example.bff_inventario.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
        name = "inventariosGraphQLClient",
        url  = "${azure.functions.inventarios}"
)
public interface InventarioGraphQLClient {
    @PostMapping(value = "${spring.rutas.graphql.base.inventarios}", consumes = "application/json")
    Map<String, Object> exec(@RequestBody Map<String, Object> body);
}
