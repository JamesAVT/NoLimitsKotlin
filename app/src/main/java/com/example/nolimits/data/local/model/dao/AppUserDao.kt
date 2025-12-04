package com.example.nolimits.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nolimits.data.local.model.AppUser

@Dao
interface AppUserDao {

    // Registrar usuario
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: AppUser)

    // Comprobar si un correo ya existe (evitar duplicados)
    @Query("SELECT * FROM app_users WHERE correo = :correo LIMIT 1")
    suspend fun getUserByCorreo(correo: String): AppUser?

    // Inicio de sesión (correo + contraseña correctos)
    @Query("SELECT * FROM app_users WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): AppUser?
}
