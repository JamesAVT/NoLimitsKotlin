package com.example.nolimits.data.repository

import com.example.nolimits.data.local.dao.AppUserDao
import com.example.nolimits.data.remote.api.ApiClient
import com.example.nolimits.data.remote.dto.LoginRequest
import com.example.nolimits.data.remote.dto.UsuarioRegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ============================================================================
// AuthRepository.kt
// ----------------------------------------------------------------------------
// Este repositorio centraliza TODA la lógica relacionada con autenticación:
//
//  - Login contra el backend
//  - Registro de usuarios (API + Room)
//  - Validación de correo (para recuperar contraseña)
//
// Es la "capa de datos" del módulo de autenticación:
// El ViewModel NO debería llamar directamente a Retrofit ni a Room,
// siempre se comunica a través de este repositorio.
// =========================================================================
class AuthRepository(
    private val appUserDao: AppUserDao
) {

    private val api = ApiClient.service

    // =========================================================================
    // LOGIN
    // -------------------------------------------------------------------------
    // Realiza una petición POST al backend con correo y contraseña.
    // Si el login es exitoso, el servidor devuelve un JSON que contiene el ID
    // del usuario logueado. Ese ID lo usamos después para guardar la sesión.
    //
    // return:
    //   -> ID del usuario (si login correcto)
    //   -> -1 si falla el login o si el backend responde error
    // =========================================================================
    suspend fun login(correo: String, clave: String): Int {
        val body = LoginRequest(correo = correo, password = clave)
        // Llamada HTTP
        val response = api.login(body)

        // Si la API respondió correctamente (HTTP 200)
        return if (response.isSuccessful) {

            try {
                // Obtiene el JSON
                val json = response.body()?.string()

                // Extraer el número de "id": xxx usando Regex
                val id = Regex("\"id\"\\s*:\\s*(\\d+)").find(json ?: "")?.groupValues?.get(1)?.toInt()
                id ?: -1 // si id es null → retorno -1

            } catch (e: Exception) {
                -1
            }

        } else {
            -1 // Login fallido
        }
    }

    // =========================================================================
    // REGISTER (API + guardar información en Room)
    // -------------------------------------------------------------------------
    // Flujo de registro:
    //   1. Enviar datos al backend -> crea el usuario real en tu API.
    //   2. La API devuelve un JSON con su nuevo ID.
    //   3. Devolvemos el ID al ViewModel para continuar el proceso.
    //
    // NO guardamos aquí en Room porque nuestra app usa Room para otras
    // funciones, pero se deja preparada por si queremos persistir usuarios.
    //
    // return:
    //   -> ID del usuario creado
    //   -> null si falla el registro
    // =========================================================================
    suspend fun register(
        nombre: String,
        apellidos: String,
        correo: String,
        telefono: Long,
        password: String
    ): Int? = withContext(Dispatchers.IO) {

        // Construcción del objeto enviado al backend
        val body = UsuarioRegisterRequest(
            nombre = nombre,
            apellidos = apellidos,
            correo = correo,
            telefono = telefono,
            password = password,
            rolId = 1L
        )

        val response = api.registerUser(body)

        // Si la creación falló → null
        if (!response.isSuccessful) return@withContext null

        // Obtener JSON devuelto
        val json = response.body()?.string() ?: return@withContext null

        // Extraer el ID nuevo del usuario
        val id = Regex("\"id\":\\s*(\\d+)").find(json)?.groupValues?.get(1)?.toInt()

        return@withContext id
    }

    // =========================================================================
    // VALIDAR SI UN CORREO EXISTE
    // -------------------------------------------------------------------------
    // Se usa en:
    //     Recuperar contraseña (Forgot Password)
    //
    // Lógica:
    //   GET /api/v1/usuarios/correo/{correo}
    //
    // Respuestas esperadas del backend:
    //   -> 200 = el correo existe
    //   -> 404 = el correo NO existe
    //
    // return:
    //   -> true si existe
    //   -> false si no existe o si ocurre algún error
    // =========================================================================
    suspend fun emailExiste(correo: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = api.getUsuarioPorCorreo(correo)

            when {
                response.isSuccessful -> true   // 200 -> existe
                response.code() == 404 -> false // 404 -> NO existe
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }
}