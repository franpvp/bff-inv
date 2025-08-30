package com.example.bff_inventario.dto;

import java.util.Objects;

public class InventarioDto {
    private Long id;
    private Long idProducto;
    private Integer cantidadProductos;
    private Long idBodega;

    public InventarioDto() {
    }

    public InventarioDto(Long id, Long idProducto, Integer cantidadProductos, Long idBodega) {
        this.id = id;
        this.idProducto = idProducto;
        this.cantidadProductos = cantidadProductos;
        this.idBodega = idBodega;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(Integer cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public Long getIdBodega() {
        return idBodega;
    }

    public void setIdBodega(Long idBodega) {
        this.idBodega = idBodega;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventarioDto that = (InventarioDto) o;
        return Objects.equals(id, that.id) && Objects.equals(idProducto, that.idProducto) && Objects.equals(cantidadProductos, that.cantidadProductos) && Objects.equals(idBodega, that.idBodega);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idProducto, cantidadProductos, idBodega);
    }
}
