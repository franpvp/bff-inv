package com.example.bff_inventario.service.impl;

import com.example.bff_inventario.dto.ProductoDto;
import com.example.bff_inventario.service.GraphqlExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductoServiceImpl {

    private final GraphqlExecutor gql;

    public ProductoServiceImpl(GraphqlExecutor gql) {
        this.gql = gql;
    }

    public ProductoDto obtenerPorId(Long id) {
        String q = """
            query($id: ID!) {
              producto(id: $id) { id nombre descripcion precio }
            }
        """;
        Map<String, Object> resp = gql.ejecutar(q, Map.of("id", id));
        Map<String, Object> map = gql.getData(resp, "producto");
        return map == null ? null : toDto(map);
    }

    public List<ProductoDto> listar() {
        String q = """
            query {
              productos { id nombre descripcion precio }
            }
        """;
        Map<String, Object> resp = gql.ejecutar(q, Map.of());
        List<Map<String, Object>> list = gql.getData(resp, "productos");
        if (list == null) return List.of();
        return list.stream().map(this::toDto).toList();
    }

    public ProductoDto crear(ProductoDto input) {
        String m = """
            mutation($input: ProductoInput!) {
              crearProducto(input: $input) { id nombre descripcion precio }
            }
        """;
        Map<String, Object> vars = Map.of(
                "input", Map.of(
                        "nombre", input.getNombre(),
                        "descripcion", input.getDescripcion(),
                        "precio", input.getPrecio()
                )
        );
        Map<String, Object> resp = gql.ejecutar(m, vars);
        Map<String, Object> map = gql.getData(resp, "crearProducto");
        return toDto(map);
    }

    public ProductoDto actualizar(Long id, ProductoDto input) {
        String m = """
            mutation($id: ID!, $input: ProductoInput!) {
              actualizarProducto(id: $id, input: $input) { id nombre descripcion precio }
            }
        """;
        Map<String, Object> vars = Map.of(
                "id", id,
                "input", Map.of(
                        "nombre", input.getNombre(),
                        "descripcion", input.getDescripcion(),
                        "precio", input.getPrecio()
                )
        );
        Map<String, Object> resp = gql.ejecutar(m, vars);
        Map<String, Object> map = gql.getData(resp, "actualizarProducto");
        return map == null ? null : toDto(map);
    }

    public boolean eliminar(Long id) {
        String m = """
            mutation($id: ID!) {
              eliminarProducto(id: $id)
            }
        """;
        Map<String, Object> resp = gql.ejecutar(m, Map.of("id", id));
        Boolean ok = gql.getData(resp, "eliminarProducto");
        return ok != null && ok;
    }

    private ProductoDto toDto(Map<String, Object> map) {
        ProductoDto p = new ProductoDto();
        p.setId(Long.valueOf(String.valueOf(map.get("id"))));
        p.setNombre((String) map.get("nombre"));
        p.setDescripcion((String) map.get("descripcion"));
        Object precioObj = map.get("precio");
        if (precioObj != null) p.setPrecio(Integer.parseInt(String.valueOf(precioObj)));
        return p;
    }
}