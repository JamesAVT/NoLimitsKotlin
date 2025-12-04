// Ruta: app/src/main/java/com/example/nolimits/data/repository/AuthRepository.kt
package com.example.nolimits.data.repository

import com.example.nolimits.data.local.dao.AppUserDao
import com.example.nolimits.data.local.model.AppUser
import com.example.nolimits.data.remote.api.ApiClient
import com.example.nolimits.data.remote.dto.LoginRequest
import com.example.nolimits.data.remote.dto.UsuarioRegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AuthRepository(
    private val appUserDao: AppUserDao
) {

    private val api = ApiClient.service

    // ========================
    // LOGIN contra la API
    // ========================
    suspend fun login(correo: String, clave: String): Int {
        val body = LoginRequest(correo = correo, password = clave)
        val response = api.login(body)

        return if (response.isSuccessful) {

            try {
                val json = response.body()?.string()

                val id = Regex("\"id\"\\s*:\\s*(\\d+)").find(json ?: "")?.groupValues?.get(1)?.toInt()
                id ?: -1

            } catch (e: Exception) {
                -1
            }

        } else {
            -1
        }
    }


    // ========================
    // REGISTER (API + guardar en Room)
    // ========================
    suspend fun register(
        nombre: String,
        apellidos: String,
        correo: String,
        telefono: Long,
        password: String
    ): Int? = withContext(Dispatchers.IO) {

        val body = UsuarioRegisterRequest(
            nombre = nombre,
            apellidos = apellidos,
            correo = correo,
            telefono = telefono,
            password = password,
            rolId = 1L
        )

        val response = api.registerUser(body)

        if (!response.isSuccessful) return@withContext null

        // Usamos el body de la respuesta:
        val json = response.body()?.string() ?: return@withContext null

        // Extraemos el id del JSON (simple)
        val id = Regex("\"id\":\\s*(\\d+)").find(json)?.groupValues?.get(1)?.toInt()

        return@withContext id
    }
}