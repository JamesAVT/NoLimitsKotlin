package com.example.nolimits.data.remote.dto

import com.example.nolimits.domain.Product
import com.google.gson.annotations.SerializedName

// =======================================================================
// ProductoResumenDto.kt
// -----------------------------------------------------------------------
// DTO (Data Transfer Object) que representa un "resumen de producto"
// tal como viene desde el backend.
//
// Este formato viene directamente del endpoint:
//     GET /api/v1/productos/resumen
//
// El backend devuelve un JSON con claves como:
//
//   {
//       "ID": 57,
//       "Nombre": "Spiderman-1",
//       "Precio": 9990,
//       "Tipo Producto": "Película",
//       "Estado": "Disponible"
//   }
//
// Retrofit + Gson convierten ese JSON en un objeto ProductoResumenDto,
// gracias a @SerializedName que indica exactamente cómo mapear cada campo.
// =======================================================================
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

// =======================================================================
// Conversión a Product (modelo usado internamente en Android)
// -----------------------------------------------------------------------
// Android NO usa directamente ProductoResumenDto en la UI.
// Para eso existe el modelo Product (id, name, price, tipoProducto, imagen).
//
// Esta función transforma el DTO a Product.
// =======================================================================
fun ProductoResumenDto.toProduct(): Product {
    return Product(
        id = this.id.toInt(),               // el backend usa Long, Android usa Int
        name = this.nombre,                 // nombre tal cual viene del backend
        price = this.precio,                // precio en Double
        tipoProducto = this.tipoProducto,   // esencial para categorizar
        imageRes = null                     // imagen se asigna después en el Repository
    )
}
