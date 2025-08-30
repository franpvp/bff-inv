package com.example.bff_inventario.controller;

import com.example.bff_inventario.dto.ProductoDto;
import com.example.bff_inventario.feign.ProductoFeign;
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
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoFeign productoFeign;

    public ProductoController(ProductoFeign productoFeign) {
        this.productoFeign = productoFeign;
    }

    @GetMapping
    public List<ProductoDto> obtenerProductos() {
        return productoFeign.obtenerProductos();
    }

    @GetMapping("/{id}")
    public ProductoDto obtenerProductoById(@PathVariable("id") Long id){
        return productoFeign.obtenerProductoById(id);
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
