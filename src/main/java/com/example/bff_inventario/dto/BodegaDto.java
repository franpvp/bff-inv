package com.example.bff_inventario.dto;

import com.example.bff_inventario.enums.BodegaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodegaDto {

    private Long id;
    private String nombre;
    private String direccion;
    private Integer capacidad;
    private List<ProductoDto> productoDtoList;
    private BodegaEnum bodegaEnum;

}
