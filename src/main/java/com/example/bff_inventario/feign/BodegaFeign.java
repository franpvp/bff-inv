package com.example.bff_inventario.feign;

import com.example.bff_inventario.dto.BodegaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "bodegaFeign",
        url = "${azure.functions.bodegas}"
)
public interface BodegaFeign {

    @GetMapping("${spring.rutas.base.bodegas}")
    List<BodegaDto> obtenerBodegas();

    @GetMapping("${spring.rutas.base.bodegas}/{id}")
    BodegaDto obtenerProductosByIdBodega(@PathVariable("id") Long id);

    @PostMapping("${spring.rutas.base.bodegas}")
    void crearBodega(@RequestBody BodegaDto bodegaDto);

    @PutMapping("${spring.rutas.base.bodegas}/{id}")
    void modificarDatosBodega(@RequestBody BodegaDto bodegaDto, @PathVariable("id") Long id);

    @DeleteMapping("${spring.rutas.base.bodegas}/{id}")
    void eliminarBodegaById(@PathVariable("id") Long id);
}
