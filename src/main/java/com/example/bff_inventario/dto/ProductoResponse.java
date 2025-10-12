package com.example.bff_inventario.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponse {
    private String nombre;
    private Long id;
    private String descripcion;
    private Integer precio;
}
