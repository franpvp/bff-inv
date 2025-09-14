package com.example.bff_inventario.service.impl;

import com.example.bff_inventario.dto.InventarioDto;
import com.example.bff_inventario.service.GraphqlExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InventarioServiceImpl {

    private final GraphqlExecutor gql;

    public InventarioServiceImpl(GraphqlExecutor gql) {
        this.gql = gql;
    }

    public InventarioDto obtenerPorId(Long id) {
        String q = """
            query($id: ID!) {
              inventario(id: $id) { id idProducto cantidadProductos idBodega }
            }
        """;
        Map<String, Object> resp = gql.ejecutarInventarios(q, Map.of("id", id));
        Map<String, Object> map = gql.getData(resp, "inventario");
        return map == null ? null : toDto(map);
    }

    public List<InventarioDto> listar() {
        String q = """
            query {
              inventarios { id idProducto cantidadProductos idBodega }
            }
        """;
        Map<String, Object> resp = gql.ejecutarInventarios(q, Map.of());
        List<Map<String, Object>> list = gql.getData(resp, "inventarios");
        if (list == null) return List.of();
        return list.stream().map(this::toDto).toList();
    }

    public InventarioDto crear(InventarioDto input) {
        String m = """
            mutation($input: InventarioInput!) {
              crearInventario(input: $input) {
                id idProducto cantidadProductos idBodega
              }
            }
        """;
        Map<String, Object> vars = Map.of(
                "input", Map.of(
                        "idProducto", input.getIdProducto(),
                        "cantidadProductos", input.getCantidadProductos(),
                        "idBodega", input.getIdBodega()
                )
        );
        Map<String, Object> resp = gql.ejecutarInventarios(m, vars);
        Map<String, Object> map = gql.getData(resp, "crearInventario");
        return toDto(map);
    }

    public InventarioDto actualizar(Long id, InventarioDto input) {
        String m = """
            mutation($id: ID!, $input: InventarioInput!) {
              actualizarInventario(id: $id, input: $input) {
                id idProducto cantidadProductos idBodega
              }
            }
        """;
        Map<String, Object> vars = Map.of(
                "id", id,
                "input", Map.of(
                        "idProducto", input.getIdProducto(),
                        "cantidadProductos", input.getCantidadProductos(),
                        "idBodega", input.getIdBodega()
                )
        );
        Map<String, Object> resp = gql.ejecutarInventarios(m, vars);
        Map<String, Object> map = gql.getData(resp, "actualizarInventario");
        return map == null ? null : toDto(map);
    }

    public boolean eliminar(Long id) {
        String m = """
            mutation($id: ID!) {
              eliminarInventario(id: $id)
            }
        """;
        Map<String, Object> resp = gql.ejecutarInventarios(m, Map.of("id", id));
        Boolean ok = gql.getData(resp, "eliminarInventario");
        return ok != null && ok;
    }

    private InventarioDto toDto(Map<String, Object> map) {
        InventarioDto d = new InventarioDto();
        Object idObj = map.get("id");
        if (idObj != null) d.setId(Long.valueOf(String.valueOf(idObj)));

        Object idProd = map.get("idProducto");
        if (idProd != null) d.setIdProducto(Long.valueOf(String.valueOf(idProd)));

        Object cant = map.get("cantidadProductos");
        if (cant != null) d.setCantidadProductos(Integer.valueOf(String.valueOf(cant)));

        Object idBod = map.get("idBodega");
        if (idBod != null) d.setIdBodega(Long.valueOf(String.valueOf(idBod)));

        return d;
    }
}