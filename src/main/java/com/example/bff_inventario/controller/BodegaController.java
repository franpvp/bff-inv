package com.example.bff_inventario.controller;

import com.example.bff_inventario.dto.ApiError;
import com.example.bff_inventario.dto.BodegaDto;
import com.example.bff_inventario.dto.ResponseDto;
import com.example.bff_inventario.feign.BodegaFeign;
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
@RequestMapping("${spring.rutas.base.bodegas}")
public class BodegaController {

    private static final Logger log = LoggerFactory.getLogger(BodegaController.class);
    private final BodegaFeign bodegaFeign;

    public BodegaController(BodegaFeign bodegaFeign) {
        this.bodegaFeign = bodegaFeign;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<BodegaDto>>> obtenerBodegas() {
        try {
            List<BodegaDto> listaBodegas = bodegaFeign.obtenerBodegas();

            if (listaBodegas == null || listaBodegas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ResponseDto.<List<BodegaDto>>builder()
                                .status(HttpStatus.NO_CONTENT.value())
                                .mensaje("No registro de bodegas en BD")
                                .response(null)
                                .build());
            }

            return ResponseEntity.ok(
                    ResponseDto.<List<BodegaDto>>builder()
                            .status(HttpStatus.OK.value())
                            .mensaje("Obtención de listaBodegas exitosa")
                            .response(listaBodegas)
                            .build()
            );
        } catch (FeignException e) {
            String detalle = e.contentUTF8();
            HttpStatus status;
            try {
                status = HttpStatus.valueOf(e.status());
            } catch (Exception ignore) {
                status = HttpStatus.BAD_GATEWAY;
            }

            return ResponseEntity.status(status)
                    .body(ResponseDto.<List<BodegaDto>>builder()
                            .status(status.value())
                            .error("Fallo al consultar servicio de bodegas")
                            .mensaje(detalle != null && !detalle.isBlank() ? detalle : "Dependencia no disponible")
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerBodegasById(@PathVariable("id") Long id) {
        final String path = Utilidad.BASE_DIR_BODEGA + Utilidad.BASE_DIR + id;
        try {
            BodegaDto dto = bodegaFeign.obtenerProductosByIdBodega(id);
            return ResponseEntity.ok(dto);
        } catch (FeignException.NotFound nf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.of(404, "Not Found",
                            "Bodega no encontrada con ID " + id, path));
        } catch (FeignException ex) {
            return Utilidad.mapFeignError(ex, path,
                    "No fue posible obtener la bodega solicitada.");
        }
    }

    @PostMapping
    public ResponseEntity<?> crearBodega(@RequestBody BodegaDto bodegaDto) {
        try {
            bodegaFeign.crearBodega(bodegaDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (FeignException.BadRequest br) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.of(400, "Bad Request",
                            "Datos inválidos para crear la bodega.", Utilidad.BASE_DIR_BODEGA));
        } catch (FeignException.Conflict cf) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiError.of(409, "Conflict",
                            "Conflicto al crear la bodega (posible duplicado).", Utilidad.BASE_DIR_BODEGA));
        } catch (FeignException ex) {
            return Utilidad.mapFeignError(ex, Utilidad.BASE_DIR_BODEGA,
                    "No fue posible crear la bodega.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarBodegaById(@RequestBody BodegaDto bodegaDto,
                                                 @PathVariable("id") Long id) {
        final String path = Utilidad.BASE_DIR_BODEGA + Utilidad.BASE_DIR +  id;
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
            return Utilidad.mapFeignError(ex, path,
                    "No fue posible modificar la bodega.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarBodegaById(@PathVariable("id") Long id) {
        final String path = Utilidad.BASE_DIR_BODEGA + Utilidad.BASE_DIR + id;
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
            return Utilidad.mapFeignError(ex, path,
                    "No fue posible eliminar la bodega.");
        }
    }
}
