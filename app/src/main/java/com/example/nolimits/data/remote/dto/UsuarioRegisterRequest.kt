package com.example.nolimits.data.remote.dto

// ==========================================================================
// UsuarioRegisterRequest.kt
// --------------------------------------------------------------------------
// DTO que se envía al backend cuando un usuario se registra.
//
// Se usa en la llamada:
//      POST /api/v1/usuarios
//
// Android construye esta clase con los datos ingresados por el usuario en
// RegistroScreen, y Retrofit la convierte automáticamente en JSON.
//
//
// El backend recibe este cuerpo y crea el usuario en la base de datos.
// ==========================================================================
data class UsuarioRegisterRequest(
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val telefono: Long,
    val password: String,
    val rolId: Long
)