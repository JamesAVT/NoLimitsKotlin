package com.example.nolimits.data.remote.dto

// =====================================================================
// DetalleVentaRequest.kt
// ---------------------------------------------------------------------
// DTO (Data Transfer Object) que representa un ítem dentro de una venta.
//
// Cada venta puede tener uno o varios productos.
// Este objeto describe 1 producto comprado, incluyendo:
//   - el ID del producto
//   - la cantidad comprada
//   - el precio unitario al momento de la compra
//
// Este DTO es enviado en la lista "detalles" dentro de VentaRequest.
// Retrofit lo convierte automáticamente a JSON cuando se envía al backend.
// =====================================================================
data class DetalleVentaRequest(
    val productoId: Int,
    val cantidad: Int,
    val precioUnitario: Int
)
