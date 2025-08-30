package com.example.bff_inventario.dto;

import java.util.Objects;

public class BodegaDto {
    private Long id;
    private String nombre;
    private String direccion;

    public BodegaDto() {
    }

    public BodegaDto(Long id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BodegaDto bodegaDto = (BodegaDto) o;
        return Objects.equals(id, bodegaDto.id) && Objects.equals(nombre, bodegaDto.nombre) && Objects.equals(direccion, bodegaDto.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, direccion);
    }
}
