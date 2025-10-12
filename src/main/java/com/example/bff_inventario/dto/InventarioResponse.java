package com.example.bff_inventario.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponse {
    private Integer cantidadProductos;
    private Long idBodega;
    private Long idInventario;
}
