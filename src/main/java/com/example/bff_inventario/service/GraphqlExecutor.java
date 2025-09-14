package com.example.bff_inventario.service;

import com.example.bff_inventario.feign.BodegaGraphQLClient;
import com.example.bff_inventario.feign.InventarioGraphQLClient;
import com.example.bff_inventario.feign.ProductosGraphQLClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GraphqlExecutor {

    private final ProductosGraphQLClient productosGraphQLClient;
    private final InventarioGraphQLClient inventarioGraphQLClient;
    private final BodegaGraphQLClient bodegaGraphQLClient;

    public GraphqlExecutor(ProductosGraphQLClient productosGraphQLClient,
                           InventarioGraphQLClient inventarioGraphQLClient, BodegaGraphQLClient bodegaGraphQLClient) {
        this.productosGraphQLClient = productosGraphQLClient;
        this.inventarioGraphQLClient = inventarioGraphQLClient;
        this.bodegaGraphQLClient = bodegaGraphQLClient;
    }

    public Map<String, Object> ejecutar(String document, Map<String, Object> variables) {
        return ejecutarProductos(document, variables);
    }

    public Map<String, Object> ejecutarProductos(String document, Map<String, Object> variables) {
        Map<String, Object> body = (variables == null || variables.isEmpty())
                ? Map.of("query", document)
                : Map.of("query", document, "variables", variables);

        Map<String, Object> resp = productosGraphQLClient.exec(body);
        validarErrores(resp);
        return resp;
    }

    public Map<String, Object> ejecutarInventarios(String document, Map<String, Object> variables) {
        Map<String, Object> body = (variables == null || variables.isEmpty())
                ? Map.of("query", document)
                : Map.of("query", document, "variables", variables);

        Map<String, Object> resp = inventarioGraphQLClient.exec(body);
        validarErrores(resp);
        return resp;
    }

    public Map<String, Object> ejecutarBodegas(String document, Map<String, Object> variables) {
        Map<String, Object> body = (variables == null || variables.isEmpty())
                ? Map.of("query", document)
                : Map.of("query", document, "variables", variables);

        Map<String, Object> resp = bodegaGraphQLClient.exec(body);
        validarErrores(resp);
        return resp;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(Map<String, Object> resp, String path) {
        Object node = resp.get("data");
        for (String p : path.split("\\.")) {
            if (!(node instanceof Map)) throw new IllegalStateException("Ruta inválida: " + path);
            node = ((Map<String, Object>) node).get(p);
        }
        return (T) node;
    }

    private void validarErrores(Map<String, Object> resp) {
        Object errors = resp.get("errors");
        if (errors instanceof Iterable<?> it && it.iterator().hasNext()) {
            throw new IllegalStateException("GraphQL errors: " + errors);
        }
    }
}
