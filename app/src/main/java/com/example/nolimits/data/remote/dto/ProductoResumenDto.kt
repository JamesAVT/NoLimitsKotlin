package com.example.nolimits.data.remote.dto

import com.example.nolimits.domain.Product
import com.google.gson.annotations.SerializedName

data class ProductoResumenDto(
    @SerializedName("ID")
    val id: Long,

    @SerializedName("Nombre")
    val nombre: String,

    @SerializedName("Precio")
    val precio: Double,

    @SerializedName("Tipo Producto")
    val tipoProducto: String,

    @SerializedName("Estado")
    val estado: String
)

fun ProductoResumenDto.toProduct(): Product {
    return Product(
        id = this.id.toInt(),
        name = this.nombre,
        price = this.precio,
        tipoProducto = this.tipoProducto,
        imageRes = null
    )
}
