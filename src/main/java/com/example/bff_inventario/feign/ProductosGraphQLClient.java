package com.example.bff_inventario.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
        name = "productosGraphQLClient",
        url  = "${azure.functions.productos}"
)
public interface ProductosGraphQLClient {
    @PostMapping(value = "${spring.rutas.graphql.base.productos}", consumes = "application/json")
    Map<String, Object> exec(@RequestBody Map<String, Object> body);
}
