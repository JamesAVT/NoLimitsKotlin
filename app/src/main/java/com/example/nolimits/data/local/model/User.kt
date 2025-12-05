package com.example.nolimits.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// =============================================================
// User.kt
// -------------------------------------------------------------
// Esta clase representa la tabla "users" dentro de Room.
// Es una entidad muy simple que se usa normalmente para pruebas,
// ejemplos básicos o pequeñas listas almacenadas localmente.
//
// Cada propiedad es una columna en la base de datos interna (Room).
// =============================================================
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int
)
