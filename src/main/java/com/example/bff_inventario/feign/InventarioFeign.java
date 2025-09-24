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

    @GetMapping("${spring.rutas.base.inventario}")
    List<InventarioDto> obtenerInventarios();

    @GetMapping("${spring.rutas.base.inventario}/{id}")
    InventarioDto obtenerobtenerProductosById(@PathVariable("id") Long id);

    @PostMapping("${spring.rutas.base.inventario}")
    void crearInventario(@RequestBody InventarioDto inventarioDto);

    @PutMapping("${spring.rutas.base.inventario}/{id}")
    void modificarDatosInventario(@RequestBody InventarioDto inventarioDto, @PathVariable("id") Long id);

    @DeleteMapping("${spring.rutas.base.inventario}/{id}")
    void eliminarInventarioById(@PathVariable("id") Long id);
}
