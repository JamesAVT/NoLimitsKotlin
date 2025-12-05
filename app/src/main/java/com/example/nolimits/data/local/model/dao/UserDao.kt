package com.example.nolimits.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nolimits.data.local.model.User
import kotlinx.coroutines.flow.Flow

// ============================================================
// UserDao.kt
// ------------------------------------------------------------
// DAO = Data Access Object
//
// Esta interfaz define cómo la app interactúa con la tabla "users"
// dentro de Room (la base de datos local de Android).
//
// Las funciones del DAO NO tienen lógica de negocio: solo dicen
// "qué consulta ejecutar" o "qué insertar / actualizar".
// ============================================================
@Dao
interface UserDao {

    // ========================================================
    // 1) INSERTAR USUARIO
    // --------------------------------------------------------
    // @Insert -> Room genera automáticamente el SQL necesario.
    //
    // onConflict = REPLACE:
    //   - Si el usuario ya existe con el mismo ID,
    //     se reemplaza en vez de causar error.
    //
    // suspend -> se ejecuta en corrutina (operación de I/O).
    //
    // Parámetro:
    // - user -> instancia del modelo User que será insertado.
    // =======================================================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    // ========================================================
    // 2) OBTENER TODOS LOS USUARIOS
    // --------------------------------------------------------
    // @Query -> permite escribir SQL manualmente.
    //
    // SELECT * FROM users -> selecciona todos los registros.
    // ORDER BY id DESC -> ordena desde el más nuevo al más antiguo.
    //
    // Devuelve: Flow<List<User>>
    //   - Flow es reactivo -> si la tabla cambia, la UI se actualiza sola.
    //   - Perfecto para usar con collectAsState() en Jetpack Compose.
    // ========================================================
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): Flow<List<User>>
}
