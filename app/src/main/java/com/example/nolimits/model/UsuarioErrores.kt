package com.example.nolimits.model
/*
    Aquí guardas tus clases de datos del dominio (Product, User, etc.).
    En MVVM, esta capa representa datos y entidades del negocio.

    Es un data class con errores de validación del usuario
*/ 

data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null
)