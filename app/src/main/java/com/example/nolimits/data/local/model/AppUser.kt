package com.example.nolimits.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// =============================================================
// AppUser.kt
// -------------------------------------------------------------
// Esta clase representa la tabla local "app_users" dentro de
// Room (base de datos interna de Android).
//
// Cada propiedad es una columna de la tabla.
//
// IMPORTANTE:
//  - Room NO guarda objetos complejos, solo tipos simples.
//  - Esta entidad se usa para almacenar usuarios en el dispositivo,
//    pero NO reemplaza al usuario de la API. Es solo una copia local.
// =============================================================
@Entity(tableName = "app_users")
data class AppUser(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val telefono: String,
    val direccion: String,
    val clave: String
)
