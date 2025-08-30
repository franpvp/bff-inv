package com.example.bff_inventario.controller;

import com.example.bff_inventario.dto.BodegaDto;
import com.example.bff_inventario.dto.ProductoDto;
import com.example.bff_inventario.feign.BodegaFeign;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bodegas")
public class BodegaController {

    private final BodegaFeign bodegaFeign;

    public BodegaController(BodegaFeign bodegaFeign) {
        this.bodegaFeign = bodegaFeign;
    }

    @GetMapping
    public BodegaDto obtenerBodegasById(@PathVariable Long id) {
        return bodegaFeign.obtenerProductosByIdBodega(id);
    }

    @PostMapping("/crear")
    public BodegaDto crearProducto(@RequestBody BodegaDto bodegaDto) {
        return bodegaFeign.crearBodega(bodegaDto);
    }

    @PutMapping("/modificar/{id}")
    public BodegaDto modificarBodegaById(@RequestBody BodegaDto bodegaDto, @PathVariable Long id) {
        return bodegaFeign.modificarDatosBodega(bodegaDto, id);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarProductoById(@PathVariable Long id) {
        bodegaFeign.eliminarBodegaById(id);
    }
}
