package com.example.bff_inventario.feign;

import com.example.bff_inventario.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
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

    @GetMapping("/api/productos")
    List<ProductoDto> obtenerProductos();

    @GetMapping("/api/productos/{id}")
    ProductoDto obtenerProductoById(@PathVariable("id") Long id);

    @PostMapping("/api/productos")
    void crearProducto(@RequestBody ProductoDto productoDto);

    @PutMapping("/api/productos/{id}")
    void modificarProductoById(@RequestBody ProductoDto productoDto, @PathVariable("id") Long id);

    @DeleteMapping("/api/productos/{id}")
    void eliminarProductoById(@PathVariable("id") Long id);
}
