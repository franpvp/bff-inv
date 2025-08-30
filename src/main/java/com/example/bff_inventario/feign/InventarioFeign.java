package com.example.bff_inventario.feign;

import com.example.bff_inventario.dto.InventarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "inventarioFeign",
        url = "${azure.functions.inventarios}"
)
public interface InventarioFeign {

    @GetMapping("/api/inventarios")
    List<InventarioDto> obtenerInventarios();

    @GetMapping("/api/inventarios/{id}")
    InventarioDto obtenerobtenerProductosById(@PathVariable("id") Long id);

    @PostMapping("/api/inventarios")
    void crearInventario(@RequestBody InventarioDto inventarioDto);

    @PutMapping("/api/inventarios/{id}")
    void modificarDatosInventario(@RequestBody InventarioDto inventarioDto, @PathVariable("id") Long id);

    @DeleteMapping("/api/inventarios/{id}")
    void eliminarInventarioById(@PathVariable("id") Long id);
}
