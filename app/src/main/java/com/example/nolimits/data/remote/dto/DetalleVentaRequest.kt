package com.example.nolimits.data.remote.dto

data class DetalleVentaRequest(
    val productoId: Int,
    val cantidad: Int,
    val precioUnitario: Int
)
