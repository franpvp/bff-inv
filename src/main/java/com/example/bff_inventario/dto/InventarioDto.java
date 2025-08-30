package com.example.bff_inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDto {
    private Long idProducto;
    private Integer cantidadProductos;
    private Long idBodega;
}
