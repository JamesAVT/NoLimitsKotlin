package com.example.nolimits.data.remote.dto

data class VentaRequest(
    val usuarioId: Int,
    val metodoPagoId: Int,
    val metodoEnvioId: Int,
    val estadoId: Int,
    val detalles: List<DetalleVentaRequest>
)
