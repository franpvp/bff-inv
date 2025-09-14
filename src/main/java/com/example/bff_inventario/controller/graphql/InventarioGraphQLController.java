package com.example.bff_inventario.controller.graphql;

import com.example.bff_inventario.dto.InventarioDto;
import com.example.bff_inventario.service.impl.InventarioServiceImpl;
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
@RequestMapping("/graphql/inventarios")
public class InventarioGraphQLController {

    private final InventarioServiceImpl inventarioService;

    public InventarioGraphQLController(InventarioServiceImpl inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<InventarioDto>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        InventarioDto dto = inventarioService.obtenerPorId(id);
        if (dto == null) return notFound("/graphql/inventarios/" + id, "Inventario no encontrado con ID " + id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody InventarioDto body) {
        if (body == null || body.getIdProducto() == null || body.getIdBodega() == null || body.getCantidadProductos() == null) {
            return badRequest("/inventarios", "idProducto, idBodega y cantidadProductos son obligatorios.");
        }
        InventarioDto creado = inventarioService.crear(body);
        return ResponseEntity.created(URI.create("/graphql/inventarios")).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody InventarioDto body) {
        if (body == null || body.getIdProducto() == null || body.getIdBodega() == null || body.getCantidadProductos() == null) {
            return badRequest("/graphql/inventarios/" + id, "idProducto, idBodega y cantidadProductos son obligatorios.");
        }
        InventarioDto actualizado = inventarioService.actualizar(id, body);
        if (actualizado == null) return notFound("/graphql/inventarios/" + id, "No existe inventario con ID " + id);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean ok = inventarioService.eliminar(id);
        if (!ok) return notFound("/graphql/inventarios/" + id, "No existe inventario con ID " + id);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Inventario eliminado con éxito");
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
