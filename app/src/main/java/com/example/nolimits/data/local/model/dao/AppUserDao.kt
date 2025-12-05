package com.example.nolimits.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nolimits.data.local.model.AppUser

// ============================================================================
// AppUserDao.kt
// ----------------------------------------------------------------------------
// DAO = Data Access Object
//
// Esta interfaz define TODAS las operaciones que Room puede realizar sobre la
// tabla "app_users". Room genera automáticamente la implementación.
//
// Funciones principales:
//   - Insertar usuarios (registro local)
//   - Verificar si un correo ya existe (evitar duplicados)
//   - Iniciar sesión usando correo + contraseña
// ============================================================================
@Dao
interface AppUserDao {

    // Registrar usuario
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: AppUser)

    // ------------------------------------------------------------------------
    // BUSCAR USUARIO POR CORREO
    // ------------------------------------------------------------------------
    // Esta función se usa para:
    //   - Validar si un correo ya está registrado (registro)
    //   - Recuperar datos de un usuario existente
    //
    // Room convierte el resultado directamente a AppUser o null.
    //
    // LIMIT 1 → mejora el rendimiento y garantiza devolver solo 1 fila.
    // ------------------------------------------------------------------------
    @Query("SELECT * FROM app_users WHERE correo = :correo LIMIT 1")
    suspend fun getUserByCorreo(correo: String): AppUser?

    // ------------------------------------------------------------------------
    // LOGIN LOCAL (correo + contraseña)
    // ------------------------------------------------------------------------
    // Esta consulta valida si existen un usuario con:
    //   → ese correo
    //   → esa contraseña
    //
    // Si coincide: devuelve el usuario completo (AppUser)
    // Si no coincide: devuelve null
    //
    // ------------------------------------------------------------------------
    @Query("SELECT * FROM app_users WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): AppUser?
}
