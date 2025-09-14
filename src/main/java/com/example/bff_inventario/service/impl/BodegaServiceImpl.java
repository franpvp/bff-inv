package com.example.bff_inventario.service.impl;

import com.example.bff_inventario.dto.BodegaDto;
import com.example.bff_inventario.service.GraphqlExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BodegaServiceImpl {

    private final GraphqlExecutor gql;

    public BodegaServiceImpl(GraphqlExecutor gql) {
        this.gql = gql;
    }

    public BodegaDto obtenerPorId(Long id) {
        String q = """
            query($id: ID!) {
              bodega(id: $id) { id nombre direccion }
            }
        """;
        Map<String, Object> resp = gql.ejecutarBodegas(q, Map.of("id", id));
        Map<String, Object> map = gql.getData(resp, "bodega");
        return map == null ? null : toDto(map);
    }

    public List<BodegaDto> listar() {
        String q = """
            query {
              bodegas { id nombre direccion }
            }
        """;
        Map<String, Object> resp = gql.ejecutarBodegas(q, Map.of());
        List<Map<String, Object>> list = gql.getData(resp, "bodegas");
        if (list == null) return List.of();
        return list.stream().map(this::toDto).toList();
    }

    public BodegaDto crear(BodegaDto input) {
        String m = """
            mutation($input: BodegaInput!) {
              crearBodega(input: $input) { id nombre direccion }
            }
        """;
        Map<String, Object> vars = Map.of(
                "input", Map.of(
                        "nombre", input.getNombre(),
                        "direccion", input.getDireccion()
                )
        );
        Map<String, Object> resp = gql.ejecutarBodegas(m, vars);
        Map<String, Object> map = gql.getData(resp, "crearBodega");
        return toDto(map);
    }

    public BodegaDto actualizar(Long id, BodegaDto input) {
        String m = """
            mutation($id: ID!, $input: BodegaInput!) {
              actualizarBodega(id: $id, input: $input) { id nombre direccion }
            }
        """;
        Map<String, Object> vars = Map.of(
                "id", id,
                "input", Map.of(
                        "nombre", input.getNombre(),
                        "direccion", input.getDireccion()
                )
        );
        Map<String, Object> resp = gql.ejecutarBodegas(m, vars);
        Map<String, Object> map = gql.getData(resp, "actualizarBodega");
        return map == null ? null : toDto(map);
    }

    public boolean eliminar(Long id) {
        String m = """
            mutation($id: ID!) {
              eliminarBodega(id: $id)
            }
        """;
        Map<String, Object> resp = gql.ejecutarBodegas(m, Map.of("id", id));
        Boolean ok = gql.getData(resp, "eliminarBodega");
        return ok != null && ok;
    }

    private BodegaDto toDto(Map<String, Object> map) {
        BodegaDto b = new BodegaDto();
        Object idObj = map.get("id");
        if (idObj != null) b.setId(Long.valueOf(String.valueOf(idObj)));
        b.setNombre((String) map.get("nombre"));
        b.setDireccion((String) map.get("direccion"));
        return b;
    }
}