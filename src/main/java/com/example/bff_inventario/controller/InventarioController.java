package com.example.bff_inventario.controller;

import com.example.bff_inventario.dto.ApiError;
import com.example.bff_inventario.dto.InventarioDto;
import com.example.bff_inventario.feign.InventarioFeign;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {

    private final InventarioFeign inventarioFeign;

    public InventarioController(InventarioFeign inventarioFeign) {
        this.inventarioFeign = inventarioFeign;
    }

    @GetMapping
    public ResponseEntity<?> obtenerInventarios() {
        final String path = "/api/inventarios";
        try {
            List<InventarioDto> lista = inventarioFeign.obtenerInventarios();
            return ResponseEntity.ok(lista);
        } catch (FeignException ex) {
            return mapFeignError(ex, path, "No fue posible obtener los inventarios.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInventarioById(@PathVariable("id") Long id) {
        final String path = "/api/inventarios/" + id;
        try {
            InventarioDto dto = inventarioFeign.obtenerobtenerProductosById(id);
            return ResponseEntity.ok(dto);
        } catch (FeignException.NotFound nf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.of(404, "Not Found",
                            "Inventario no encontrado con ID " + id, path));
        } catch (FeignException ex) {
            return mapFeignError(ex, path, "No fue posible obtener el inventario solicitado.");
        }
    }

    @PostMapping
    public ResponseEntity<?> crearInventario(@RequestBody InventarioDto inventarioDto) {
        final String path = "/api/inventarios";
        try {
            inventarioFeign.crearInventario(inventarioDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (FeignException.BadRequest br) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.of(400, "Bad Request",
                            "Datos inválidos para crear el inventario.", path));
        } catch (FeignException.Conflict cf) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiError.of(409, "Conflict",
                            "Conflicto al crear el inventario.", path));
        } catch (FeignException ex) {
            return mapFeignError(ex, path, "No fue posible crear el inventario.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarInventario(@RequestBody InventarioDto inventarioDto,
                                                 @PathVariable("id") Long id) {
        final String path = "/api/inventarios/" + id;
        try {
            inventarioFeign.modificarDatosInventario(inventarioDto, id);
            return ResponseEntity.noContent().build();
        } catch (FeignException.NotFound nf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.of(404, "Not Found",
                            "No existe inventario con ID " + id, path));
        } catch (FeignException.BadRequest br) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.of(400, "Bad Request",
                            "Datos inválidos para modificar el inventario.", path));
        } catch (FeignException.Conflict cf) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiError.of(409, "Conflict",
                            "Conflicto al modificar el inventario.", path));
        } catch (FeignException ex) {
            return mapFeignError(ex, path, "No fue posible modificar el inventario.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInventarioById(@PathVariable("id") Long id) {
        final String path = "/api/inventarios/" + id;
        try {
            inventarioFeign.eliminarInventarioById(id);
            return ResponseEntity.noContent().build();
        } catch (FeignException.NotFound nf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.of(404, "Not Found",
                            "No existe inventario con ID " + id, path));
        } catch (FeignException.Conflict cf) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiError.of(409, "Conflict",
                            "No se puede eliminar el inventario por conflicto de integridad.", path));
        } catch (FeignException ex) {
            return mapFeignError(ex, path, "No fue posible eliminar el inventario.");
        }
    }

    private ResponseEntity<ApiError> mapFeignError(FeignException ex, String path, String genericMessage) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) status = HttpStatus.BAD_GATEWAY;

        String title = switch (status) {
            case BAD_REQUEST -> "Bad Request";
            case UNAUTHORIZED -> "Unauthorized";
            case FORBIDDEN -> "Forbidden";
            case NOT_FOUND -> "Not Found";
            case CONFLICT -> "Conflict";
            case SERVICE_UNAVAILABLE -> "Service Unavailable";
            case GATEWAY_TIMEOUT -> "Gateway Timeout";
            default -> status.is5xxServerError() ? "Upstream Error" : status.getReasonPhrase();
        };

        return ResponseEntity.status(status)
                .body(ApiError.of(status.value(), title, genericMessage, path));
    }
}