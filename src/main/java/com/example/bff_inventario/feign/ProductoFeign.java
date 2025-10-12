package com.example.bff_inventario.feign;

import com.example.bff_inventario.dto.ProductoCreateResponse;
import com.example.bff_inventario.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "productoFeign",
        url = "${azure.functions.productos}"
)
public interface ProductoFeign {

    @GetMapping("${spring.rutas.base.productos}")
    List<ProductoDto> obtenerProductos();

    @GetMapping("${spring.rutas.base.productos}/{id}")
    ProductoDto obtenerProductoById(@PathVariable("id") Long id);

    @PostMapping("${spring.rutas.base.productos}")
    ResponseEntity<ProductoCreateResponse> crearProducto(@RequestBody ProductoDto productoDto);

    @PutMapping("${spring.rutas.base.productos}/{id}")
    void modificarProductoById(@RequestBody ProductoDto productoDto, @PathVariable("id") Long id);

    @DeleteMapping("${spring.rutas.base.productos}/{id}")
    void eliminarProductoById(@PathVariable("id") Long id);
}
