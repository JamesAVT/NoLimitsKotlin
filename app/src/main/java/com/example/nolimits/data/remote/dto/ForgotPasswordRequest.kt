package com.example.nolimits.data.remote.dto

// =====================================================================
// ForgotPasswordRequest.kt
// ---------------------------------------------------------------------
// DTO (Data Transfer Object) usado para solicitar recuperación de contraseña.
//
// Este objeto se envía al backend cuando el usuario ingresa su correo en la
// pantalla "Recuperar contraseña".
//
// Retrofit convierte automáticamente este objeto en un JSON para la API.
// Ejemplo del JSON enviado:
//
//   {
//       "email": "usuario@example.com"
//   }
//
// El backend toma ese correo y actúa según su lógica:
//   - generar token de recuperación
//   - enviar un correo
//   - validar usuario existente, etc.
// =====================================================================
data class ForgotPasswordRequest(
    val email: String
)