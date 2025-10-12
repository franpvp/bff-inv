package com.example.bff_inventario.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoCreateResponse {
    private String message;
    private InventarioResponse inventario;
    private ProductoResponse producto;
}
