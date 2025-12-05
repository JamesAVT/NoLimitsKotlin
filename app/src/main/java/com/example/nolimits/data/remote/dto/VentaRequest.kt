package com.example.nolimits.data.remote.dto

// ==========================================================================
// VentaRequest.kt
// --------------------------------------------------------------------------
// DTO que se envía al backend para registrar una venta real.
//
//
// Este DTO se construye desde CartViewModel después de que el usuario
// aprieta "Pagar" o finaliza la compra.
//
// Android envía este objeto, Retrofit lo convierte a JSON y Render (backend)
// crea en la base de datos:
//    - VentaModel
//    - Lista de DetalleVentaModel asociados (por cascada)
// ==========================================================================
data class VentaRequest(
    val usuarioId: Int,
    val metodoPagoId: Int,
    val metodoEnvioId: Int,
    val estadoId: Int,
    val detalles: List<DetalleVentaRequest>
)
