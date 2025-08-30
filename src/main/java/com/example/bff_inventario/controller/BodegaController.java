package com.example.bff_inventario.controller;

import com.example.bff_inventario.dto.ApiError;
import com.example.bff_inventario.dto.BodegaDto;
import com.example.bff_inventario.feign.BodegaFeign;
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

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/bodegas")
public class BodegaController {

    private final BodegaFeign bodegaFeign;

    public BodegaController(BodegaFeign bodegaFeign) {
        this.bodegaFeign = bodegaFeign;
    }

    @GetMapping
    public ResponseEntity<?> obtenerBodegas() {
        final String path = "/api/bodegas";
        try {
            List<BodegaDto> lista = bodegaFeign.obtenerBodegas();
            return ResponseEntity.ok(lista);
        } catch (FeignException ex) {
            return mapFeignError(ex, path,
                    "No fue posible obtener las bodegas."); // mensaje breve
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerBodegasById(@PathVariable("id") Long id) {
        final String path = "/api/bodegas/" + id;
        try {
            BodegaDto dto = bodegaFeign.obtenerProductosByIdBodega(id);
            return ResponseEntity.ok(dto);
        } catch (FeignException.NotFound nf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.of(404, "Not Found",
                            "Bodega no encontrada con ID " + id, path));
        } catch (FeignException ex) {
            return mapFeignError(ex, path,
                    "No fue posible obtener la bodega solicitada.");
        }
    }

    @PostMapping
    public ResponseEntity<?> crearBodega(@RequestBody BodegaDto bodegaDto) {
        final String path = "/api/bodegas";
        try {
            bodegaFeign.crearBodega(bodegaDto);
            // Si tu Function retorna el recurso creado, podrías devolverlo aquí.
            // Como el Feign es void, respondemos 201 sin cuerpo (o con el dto que enviaste).
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (FeignException.BadRequest br) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.of(400, "Bad Request",
                            "Datos inválidos para crear la bodega.", path));
        } catch (FeignException.Conflict cf) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiError.of(409, "Conflict",
                            "Conflicto al crear la bodega (posible duplicado).", path));
        } catch (FeignException ex) {
            return mapFeignError(ex, path,
                    "No fue posible crear la bodega.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarBodegaById(@RequestBody BodegaDto bodegaDto,
                                                 @PathVariable("id") Long id) {
        final String path = "/api/bodegas/" + id;
        try {
            bodegaFeign.modificarDatosBodega(bodegaDto, id);
            return ResponseEntity.noContent().build();
        } catch (FeignException.NotFound nf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.of(404, "Not Found",
                            "No existe bodega con ID " + id, path));
        } catch (FeignException.BadRequest br) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.of(400, "Bad Request",
                            "Datos inválidos para modificar la bodega.", path));
        } catch (FeignException.Conflict cf) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiError.of(409, "Conflict",
                            "Conflicto al modificar la bodega.", path));
        } catch (FeignException ex) {
            return mapFeignError(ex, path,
                    "No fue posible modificar la bodega.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarBodegaById(@PathVariable("id") Long id) {
        final String path = "/api/bodegas/" + id;
        try {
            bodegaFeign.eliminarBodegaById(id);
            return ResponseEntity.noContent().build();
        } catch (FeignException.NotFound nf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.of(404, "Not Found",
                            "No existe bodega con ID " + id, path));
        } catch (FeignException.Conflict cf) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiError.of(409, "Conflict",
                            "No se puede eliminar la bodega por conflicto de integridad.", path));
        } catch (FeignException ex) {
            return mapFeignError(ex, path,
                    "No fue posible eliminar la bodega.");
        }
    }

    private ResponseEntity<ApiError> mapFeignError(FeignException ex,
                                                   String path,
                                                   String genericMessage) {
        // Si el upstream mandó un status conocido, lo usamos; si no, 502
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) status = HttpStatus.BAD_GATEWAY;

        // Mensajes breves y sin stacktrace para el cliente
        String errorTitle = switch (status) {
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
                .body(ApiError.of(status.value(), errorTitle, genericMessage, path));
    }
}
