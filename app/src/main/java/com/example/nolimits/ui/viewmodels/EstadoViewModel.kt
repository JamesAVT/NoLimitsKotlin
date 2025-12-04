package com.example.nolimits.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nolimits.data.datastore.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/*
    Maneja valores guardados en EstadoDataStore.
    Ejemplo:
    - isLoggedIn
    - isDarkMode
    - token
    - nombre de usuario
*/

// ViewModel extendido desde AndroidViewModel porque usamos contexto
class EstadoViewModel(application: Application) : AndroidViewModel(application) {

    // DataStore creado con contexto de aplicación
    private val estadoDataStore = EstadoDataStore(context = application)

    // Estado que representa si está “activado” o no (observable)
    private val _activo = MutableStateFlow<Boolean?>(null)
    val activo: StateFlow<Boolean?> = _activo

    // Estado para mostrar u ocultar el mensaje animado
    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje

    // Al iniciar el ViewModel, cargamos el estado desde DataStore
    init {
        cargarEstado()
    }

    private fun cargarEstado() {
        viewModelScope.launch {
            // ⏳ Simula demora para mostrar loader (opcional)
            delay(1500)
            _activo.value = estadoDataStore.obtenerEstado().first() ?: false
        }
    }

    // Alterna el valor y lo guarda en DataStore
    fun alternarEstado() {
        viewModelScope.launch {
            // Alternamos el valor actual
            val nuevoValor = !(_activo.value ?: false)

            // Guardamos en DataStore
            estadoDataStore.guardarEstado(nuevoValor)

            // Actualizamos el flujo
            _activo.value = nuevoValor

            // Mostramos el mensaje visual animado
            _mostrarMensaje.value = true

            // Ocultamos después de 2 segundos
            delay(2000)
            _mostrarMensaje.value = false
        }
    }
}
