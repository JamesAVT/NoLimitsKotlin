package com.example.nolimits.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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
