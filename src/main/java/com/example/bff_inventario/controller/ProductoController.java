package com.example.bff_inventario.controller;

import com.example.bff_inventario.dto.ProductoDto;
import com.example.bff_inventario.feign.ProductoFeign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoFeign productoFeign;

    public ProductoController(ProductoFeign productoFeign) {
        this.productoFeign = productoFeign;
    }

    @GetMapping
    public List<ProductoDto> obtenerProductos() {
        return productoFeign.obtenerProductos();
    }

    @PostMapping
    public ProductoDto crearProducto(@RequestBody ProductoDto dto) {
        return productoFeign.crearProducto(dto);
    }


}
