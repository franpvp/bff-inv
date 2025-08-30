package com.example.bff_inventario.feign;

import com.example.bff_inventario.dto.BodegaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "bodegaFeign",
        url = "${azure.functions.bodegas}"
)
public interface BodegaFeign {

    @GetMapping("/api/bodegas")
    BodegaDto obtenerProductosByIdBodega(@PathVariable Long id);

    @PostMapping("/api/bodegas")
    BodegaDto crearBodega(@RequestBody BodegaDto bodegaDto);

    @PutMapping("/api/bodegas/{id}")
    BodegaDto modificarDatosBodega(@RequestBody BodegaDto bodegaDto, @PathVariable("id") Long id);

    @DeleteMapping("/api/bodegas/{id}")
    void eliminarBodegaById(@PathVariable("id") Long id);
}
