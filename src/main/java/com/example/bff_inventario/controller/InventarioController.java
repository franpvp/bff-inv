package com.example.bff_inventario.controller;

import com.example.bff_inventario.dto.InventarioDto;
import com.example.bff_inventario.feign.InventarioFeign;
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
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioFeign inventarioFeign;

    public InventarioController(InventarioFeign inventarioFeign) {
        this.inventarioFeign = inventarioFeign;
    }

    @GetMapping
    public List<InventarioDto> obtenerInventarios() {
        return inventarioFeign.obtenerInventarios();
    }

    @GetMapping("/{id}")
    public InventarioDto obtenerInventarioById(@PathVariable("id") Long id) {
        return inventarioFeign.obtenerobtenerProductosById(id);
    }

    @PostMapping("/crear")
    public InventarioDto crearInventario(@RequestBody InventarioDto inventarioDto) {
        return inventarioFeign.crearInventario(inventarioDto);
    }

    @PutMapping("/modificar/{id}")
    public InventarioDto modificarInventario(@RequestBody InventarioDto inventarioDto, @PathVariable Long id) {
        return inventarioFeign.modificarDatosInventario(inventarioDto, id);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarInventarioById(@PathVariable Long id) {
        inventarioFeign.eliminarInventarioById(id);
    }
}
