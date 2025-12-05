package com.example.nolimits.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nolimits.data.datastore.EstadoDataStore
import com.example.nolimits.data.local.dao.AppUserDao
import com.example.nolimits.data.repository.AuthRepository
import kotlinx.coroutines.launch

/*
    Maneja login y registro.

    - Valida correo y contraseña.
    - Llama al repositorio de autenticación (API + Room).
*/

class AuthViewModel(
    private val appUserDao: AppUserDao
) : ViewModel() {

    // AHORA SÍ le pasamos el dao al repositorio
    private val authRepo = AuthRepository(appUserDao)

    // ------------------------
    // LOGIN
    // ------------------------
    fun iniciarSesion(
        correo: String,
        clave: String,
        dataStore: EstadoDataStore,   // ← AGREGADO
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {

            val usuarioId = authRepo.login(correo, clave)

            if (usuarioId > 0) {

                // Guardar ID en DataStore
                dataStore.guardarUsuarioId(usuarioId)

                onResult(true)

            } else {
                onResult(false)
            }
        }
    }



    // ------------------------
    // REGISTRO
    // ------------------------
    fun registrarUsuario(
        nombre: String,
        apellidos: String,
        correo: String,
        telefono: String,
        direccion: String,
        clave: String,
        dataStore: EstadoDataStore,          // ← AGREGADO
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {

            val existente = appUserDao.getUserByCorreo(correo)
            if (existente != null) {
                onError("El correo ya está registrado.")
                return@launch
            }

            val telefonoLong = telefono.toLongOrNull() ?: 0L

            // Llamamos a la API → ahora devuelve el ID real
            val userId = try {
                authRepo.register(
                    nombre = nombre,
                    apellidos = apellidos,
                    correo = correo,
                    telefono = telefonoLong,
                    password = clave
                )
            } catch (e: Exception) {
                null
            }

            if (userId == null) {
                onError("Error al registrar usuario.")
                return@launch
            }

            // GUARDAR EN DATASTORE EL ID REAL
            dataStore.guardarUsuarioId(userId)

            onSuccess()
        }
    }
    // ------------------------
    // RECUPERAR CONTRASEÑA
    // ------------------------
        fun recuperarContrasena(
            correo: String,
            onSuccess: () -> Unit,
            onError: (String) -> Unit
        ) {
            viewModelScope.launch {
                // Llamamos al backend para ver si el correo existe
                val existe = authRepo.emailExiste(correo)

                if (existe) {
                    // Aquí más adelante podrías llamar a un endpoint real de "forgot password"
                    onSuccess()
                } else {
                    onError("El correo no está registrado.")
                }
            }
        }
}