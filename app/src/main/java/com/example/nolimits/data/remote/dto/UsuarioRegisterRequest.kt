package com.example.nolimits.data.remote.dto

data class UsuarioRegisterRequest(
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val telefono: Long,
    val password: String,
    val rolId: Long
)