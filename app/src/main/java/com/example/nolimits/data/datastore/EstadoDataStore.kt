package com.example.nolimits.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/*
    Fuente de datos local.
    Clase donde usas DataStore para guardar cosas simples y persistentes
*/

// Declaramos el DataStore con tipo explícito
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencias_usuario")

class EstadoDataStore(private val context: Context) {

    private val ESTADO_ACTIVADO = booleanPreferencesKey("modo_activado")

    suspend fun guardarEstado(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ESTADO_ACTIVADO] = valor
        }
    }

    fun obtenerEstado(): Flow<Boolean> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ESTADO_ACTIVADO] ?: false
        }
    }

    // Guardar id del usuario
    private val USUARIO_ID = intPreferencesKey("usuario_id")

    suspend fun guardarUsuarioId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[USUARIO_ID] = id
        }
    }

    fun obtenerUsuarioId(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[USUARIO_ID] ?: -1
        }
    }
}
