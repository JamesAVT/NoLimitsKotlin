package com.example.nolimits.data.remote.dto

// =====================================================================
// LoginRequest.kt
// ---------------------------------------------------------------------
// DTO (Data Transfer Object) usado para iniciar sesión en el sistema.
//
// Este objeto se envía al backend cuando el usuario ingresa:
//   - su correo
//   - su contraseña
//
// Retrofit convierte este objeto automáticamente en un JSON como:
//
//   {
//       "correo": "usuario@example.com",
//       "password": "12345678"
//   }
//
// El backend recibe esos datos y valida si el usuario existe y si la
// contraseña es correcta. Si todo está bien, devuelve una respuesta con:
//   - sesión iniciada
//   - datos del usuario
//   - token o cookie (según backend)
// =====================================================================
data class LoginRequest(
    val correo: String,
    val password: String
)