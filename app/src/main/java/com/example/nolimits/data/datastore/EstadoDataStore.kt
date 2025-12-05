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

// ============================================================
// Configuración de DataStore a nivel de Context
// ------------------------------------------------------------
// preferencesDataStore crea automáticamente un DataStore que
// guarda datos simples como Boolean, Int, String, etc.
//
// - name = "preferencias_usuario" → es el archivo interno donde
//   Android guardará esta información.
//
// Esta línea se coloca afuera de la clase para que cualquier
// parte de la app pueda usar "context.dataStore".
// ============================================================
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencias_usuario")

// ============================================================
// Clase que administra el acceso a DataStore
// ------------------------------------------------------------
// - Recibe un Context para poder acceder al archivo DataStore.
// - Contiene funciones para guardar/leer valores de forma segura.
// ============================================================
class EstadoDataStore(private val context: Context) {

    // ========================================================
    // 1) CLAVE PARA GUARDAR EL MODO ACTIVADO (Boolean)
    // --------------------------------------------------------
    // booleanPreferencesKey crea una clave única que permite
    // guardar un Boolean dentro del DataStore.
    //
    // Esta clave se usa como si fuera el nombre de una columna.
    // ========================================================

    private val ESTADO_ACTIVADO = booleanPreferencesKey("modo_activado")

    // ========================================================
    // GUARDAR ESTADO (Boolean)
    // --------------------------------------------------------
    // - Función suspend porque escribe en DataStore.
    // - edit { } permite modificar el archivo.
    // - preferencias[...] = valor → guarda el dato.
    // ========================================================
    suspend fun guardarEstado(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ESTADO_ACTIVADO] = valor
        }
    }

    // ========================================================
    // OBTENER ESTADO (Flow<Boolean>)
    // --------------------------------------------------------
    // - DataStore expone los datos como Flow.
    // - map { } transforma el contenido en el valor deseado.
    // - ?: false → si nunca se guardó nada, devuelve "false".
    //
    // Ventaja: cualquier cambio en DataStore se refleja
    // automáticamente en la UI usando collectAsState().
    // ========================================================
    fun obtenerEstado(): Flow<Boolean> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ESTADO_ACTIVADO] ?: false
        }
    }

    // ========================================================
    // 2) CLAVE PARA GUARDAR EL ID DEL USUARIO
    // --------------------------------------------------------
    // Se usará para recordar qué usuario está logueado.
    // ========================================================
    private val USUARIO_ID = intPreferencesKey("usuario_id")

    // ========================================================
    // GUARDAR usuarioId (Int)
    // --------------------------------------------------------
    // Guarda el ID real entregado por el backend durante login.
    // Esto permite:
    // - Registrar compras sin pedir credenciales.
    // - Recuperar la sesión.
    // ========================================================
    suspend fun guardarUsuarioId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[USUARIO_ID] = id
        }
    }

    // ========================================================
    // OBTENER usuarioId (Flow<Int>)
    // --------------------------------------------------------
    // - Si no existe, devuelve -1 (usuario no logueado).
    // - Flow permite reaccionar a cambios en la UI.
    // ========================================================
    fun obtenerUsuarioId(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[USUARIO_ID] ?: -1
        }
    }
}
