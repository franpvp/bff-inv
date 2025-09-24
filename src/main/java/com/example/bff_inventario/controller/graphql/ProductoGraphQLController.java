package com.example.bff_inventario.controller.graphql;

import com.example.bff_inventario.dto.ProductoDto;
import com.example.bff_inventario.service.impl.ProductoServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${spring.rutas.graphql.base.productos}")
public class ProductoGraphQLController {

    private final ProductoServiceImpl productoServiceImpl;

    public ProductoGraphQLController(ProductoServiceImpl productoServiceImpl) {
        this.productoServiceImpl = productoServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDto>> listar() {
        return ResponseEntity.ok(productoServiceImpl.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        ProductoDto dto = productoServiceImpl.obtenerPorId(id);
        if (dto == null) return notFound("/graphql/productos/" + id, "Producto no encontrado con ID " + id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProductoDto body) {
        if (body == null || body.getNombre() == null || body.getNombre().isBlank()) {
            return badRequest("/graphql/productos", "nombre es obligatorio.");
        }
        ProductoDto creado = productoServiceImpl.crear(body);
        return ResponseEntity.created(URI.create("/graphql/productos")).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ProductoDto body) {
        if (body == null) return badRequest("/graphql/productos/" + id, "Body requerido.");
        ProductoDto actualizado = productoServiceImpl.actualizar(id, body);
        if (actualizado == null) return notFound("/graphql/productos/" + id, "No existe producto con ID " + id);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean ok = productoServiceImpl.eliminar(id);
        if (!ok) return notFound("/graphql/productos/" + id, "No existe producto con ID " + id);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Producto eliminado con éxito");
        return ResponseEntity.ok(body);
    }

    private ResponseEntity<Map<String, Object>> badRequest(String path, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", message);
        body.put("path", path);
        return ResponseEntity.badRequest().body(body);
    }

    private ResponseEntity<Map<String, Object>> notFound(String path, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", message);
        body.put("path", path);
        return ResponseEntity.status(404).body(body);
    }

}