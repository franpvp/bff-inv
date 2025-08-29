package com.example.bff_inventario.feign;

import com.example.bff_inventario.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "productoFeign",
        url = "${azure.functions.productos}"
)
public interface ProductoFeign {

    @GetMapping("/api/ProductosFunction")
    List<ProductoDto> obtenerProductos();

    @PostMapping("/api/ProductosFunction")
    ProductoDto crearProducto(@RequestBody ProductoDto dto);
}
