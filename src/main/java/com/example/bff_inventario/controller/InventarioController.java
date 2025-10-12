package com.example.bff_inventario.controller;

import com.example.bff_inventario.dto.ApiError;
import com.example.bff_inventario.dto.InventarioDto;
import com.example.bff_inventario.dto.ResponseDto;
import com.example.bff_inventario.feign.InventarioFeign;
import com.example.bff_inventario.utils.Utilidad;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("${spring.rutas.base.inventario}")
public class InventarioController {

    private static final Logger log = LoggerFactory.getLogger(InventarioController.class);
    private final InventarioFeign inventarioFeign;

    public InventarioController(InventarioFeign inventarioFeign) {
        this.inventarioFeign = inventarioFeign;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<InventarioDto>>> obtenerInventarios() {
        try {
            List<InventarioDto> listaInventarios = inventarioFeign.obtenerInventarios();
            if (listaInventarios == null || listaInventarios.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ResponseDto.<List<InventarioDto>>builder()
                                        .status(HttpStatus.NO_CONTENT.value())
                                        .mensaje("No hay registros de inventarios en la BD")
                                        .response(null)
                                        .build());
            }
            return ResponseEntity.ok(
                    ResponseDto.<List<InventarioDto>>builder()
                            .status(HttpStatus.OK.value())
                            .mensaje("Obtención de lista de inventarios exitosa")
                            .response(listaInventarios)
                            .build());
        } catch (FeignException e) {
            HttpStatus status;
            try {
                status = HttpStatus.valueOf(e.status());
            } catch (Exception ignore) {
                status = HttpStatus.BAD_GATEWAY;
            }

            return ResponseEntity.status(status)
                    .body(ResponseDto.<List<InventarioDto>>builder()
                            .status(status.value())
                            .error("Fallo al consultar servicio de bodegas")
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInventarioById(@PathVariable("id") Long id) {
        final String path = Utilidad.BASE_DIR_INVENTARIOS + Utilidad.BASE_DIR + id;
        try {
            InventarioDto dto = inventarioFeign.obtenerobtenerProductosById(id);
            return ResponseEntity.ok(dto);
        } catch (FeignException.NotFound nf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.of(404, "Not Found",
                            "Inventario no encontrado con ID " + id, path));
        } catch (FeignException ex) {
            return Utilidad.mapFeignError(ex, path, "No fue posible obtener el inventario solicitado.");
        }
    }

    @PostMapping
    public ResponseEntity<?> crearInventario(@RequestBody InventarioDto inventarioDto) {
        try {
            inventarioFeign.crearInventario(inventarioDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (FeignException.BadRequest br) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.of(400, "Bad Request",
                            "Datos inválidos para crear el inventario.", Utilidad.BASE_DIR_INVENTARIOS));
        } catch (FeignException.Conflict cf) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiError.of(409, "Conflict",
                            "Conflicto al crear el inventario.", Utilidad.BASE_DIR_INVENTARIOS));
        } catch (FeignException ex) {
            return Utilidad.mapFeignError(ex, Utilidad.BASE_DIR_INVENTARIOS, "No fue posible crear el inventario.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarInventario(@RequestBody InventarioDto inventarioDto,
                                                 @PathVariable("id") Long id) {
        final String path = Utilidad.BASE_DIR_INVENTARIOS + Utilidad.BASE_DIR + id;
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
            return Utilidad.mapFeignError(ex, path, "No fue posible modificar el inventario.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiError> eliminarInventarioById(@PathVariable("id") Long id) {
        final String path = Utilidad.BASE_DIR_INVENTARIOS + Utilidad.BASE_DIR + id;

        try {
            inventarioFeign.eliminarInventarioById(id);
            ApiError ok = ApiError.of(
                    200,
                    "OK",
                    "Inventario con ID " + id + " eliminado correctamente.",
                    path
            );
            return ResponseEntity.ok(ok);

        } catch (FeignException.NotFound nf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.of(404, "Not Found",
                            "No existe inventario con ID " + id, path));
        } catch (FeignException.Conflict cf) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiError.of(409, "Conflict",
                            "No se puede eliminar el inventario por conflicto de integridad.", path));
        } catch (FeignException ex) {
            return Utilidad.mapFeignError(ex, path, "No fue posible eliminar el inventario.");
        }
    }
}