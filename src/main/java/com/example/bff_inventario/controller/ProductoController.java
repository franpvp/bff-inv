package com.example.bff_inventario.controller;

import com.example.bff_inventario.dto.ProductoDto;
import com.example.bff_inventario.dto.ResponseDto;
import com.example.bff_inventario.feign.ProductoFeign;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
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
import java.util.PropertyResourceBundle;

import static feign.Response.builder;

@RestController
@RequestMapping("${spring.rutas.base.productos}")
@Slf4j
public class ProductoController {

    private final ProductoFeign productoFeign;

    public ProductoController(ProductoFeign productoFeign) {
        this.productoFeign = productoFeign;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<ProductoDto>>> obtenerProductos() {
        try {
            List<ProductoDto> productoDtoList = productoFeign.obtenerProductos();
            if (productoDtoList == null || productoDtoList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ResponseDto.<List<ProductoDto>>builder()
                                .status(HttpStatus.NO_CONTENT.value())
                                .mensaje("No hay registros de productos")
                                .response(null)
                                .build());
            }
            return ResponseEntity.ok(
                    ResponseDto.<List<ProductoDto>>builder()
                            .status(HttpStatus.OK.value())
                            .mensaje("Obtencion de productos exitosa")
                            .response(productoDtoList)
                            .build()
            );


        } catch (FeignException ex) {
            String detalle = ex.contentUTF8();
            HttpStatus status;
            try {
                status = HttpStatus.valueOf(ex.status());
            } catch (Exception ignore) {
                status = HttpStatus.BAD_GATEWAY;
            }

            return ResponseEntity.status(status)
                    .body(ResponseDto.<List<ProductoDto>>builder()
                            .status(status.value())
                            .error("Fallo al consultar servicio de productos")
                            .mensaje(detalle != null && !detalle.isBlank() ? detalle : "Dependencia no disponible")
                            .response(null)
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ProductoDto>> obtenerProductoById(@PathVariable("id") Long id){
        ProductoDto productoDto;
        try {
            productoDto = productoFeign.obtenerProductoById(id);
            if (productoDto == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT.value())
                        .body(ResponseDto.<ProductoDto>builder()
                                .status(HttpStatus.NO_CONTENT.value())
                                .error(null)
                                .mensaje("No hay producto con el ID " + id)
                                .response(productoDto)
                                .build());
            }

            return ResponseEntity.ok(
                    ResponseDto.<ProductoDto>builder()
                            .status(HttpStatus.OK.value())
                            .mensaje("Obtencion del producto con ID: " + id)
                            .response(productoDto)
                            .build()
            );
        } catch (FeignException ex) {
            log.error("FeignException al obtener producto con ID: " + id);
            String detalle = ex.contentUTF8();
            HttpStatus status;
            try {
                status = HttpStatus.valueOf(ex.status());
            } catch (Exception ignore) {
                status = HttpStatus.BAD_GATEWAY;
            }
            return ResponseEntity.status(status)
                    .body(ResponseDto.<ProductoDto>builder()
                            .status(status.value())
                            .error("Fallo al consultar servicio de productos")
                            .mensaje(detalle != null && !detalle.isBlank() ? detalle : "Dependencia no disponible")
                            .response(null)
                            .build());
        }
    }

    @PostMapping
    public void crearProducto(@RequestBody ProductoDto productoDto) {
        productoFeign.crearProducto(productoDto);
    }

    @PutMapping("/{id}")
    public void modificarProductoById(@RequestBody ProductoDto productoDto, @PathVariable("id") Long id) {
        productoFeign.modificarProductoById(productoDto, id);
    }

    @DeleteMapping("/{id}")
    public void eliminarProductoById(@PathVariable Long id) {
        productoFeign.eliminarProductoById(id);
    }



}
