package com.example.bff_inventario.controller.graphql;


import com.example.bff_inventario.dto.BodegaDto;
import com.example.bff_inventario.service.impl.BodegaServiceImpl;
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
@RequestMapping("/graphql/bodegas")
public class BodegaGraphQLController {

    private final BodegaServiceImpl bodegaService;

    public BodegaGraphQLController(BodegaServiceImpl bodegaService) {
        this.bodegaService = bodegaService;
    }

    @GetMapping
    public ResponseEntity<List<BodegaDto>> listar() {
        return ResponseEntity.ok(bodegaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        BodegaDto dto = bodegaService.obtenerPorId(id);
        if (dto == null) return notFound("/graphql/bodegas/" + id, "Bodega no encontrada con ID " + id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody BodegaDto body) {
        if (body == null || body.getNombre() == null || body.getNombre().isBlank()
                || body.getDireccion() == null || body.getDireccion().isBlank()) {
            return badRequest("/bodegas", "nombre y direccion son obligatorios.");
        }
        BodegaDto creada = bodegaService.crear(body);
        return ResponseEntity.created(URI.create("/graphql/bodegas")).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody BodegaDto body) {
        if (body == null) return badRequest("/graphql/bodegas/" + id, "Body requerido.");
        BodegaDto actualizada = bodegaService.actualizar(id, body);
        if (actualizada == null) return notFound("/graphql/bodegas/" + id, "No existe bodega con ID " + id);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean ok = bodegaService.eliminar(id);
        if (!ok) return notFound("/graphql/bodegas/" + id, "No existe bodega con ID " + id);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Bodega eliminada con éxito");
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
