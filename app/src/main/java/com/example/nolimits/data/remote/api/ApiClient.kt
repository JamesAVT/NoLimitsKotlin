package com.example.nolimits.data.remote.api

import com.example.nolimits.data.remote.CookieJarImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// ==============================================================
// ApiClient.kt
// --------------------------------------------------------------
// Este objeto crea y configura el cliente HTTP que usará Retrofit
// para comunicarse con el backend.
//
// Aquí definimos:
// 1. La URL base de la API.
// 2. El cliente OkHttp con sus timeouts, logs y CookieJar.
// 3. La instancia de Retrofit que genera las llamadas del servicio.
//
// ==============================================================
object ApiClient {

    // ----------------------------------------------------------
    // URL base del backend.
    // Todas las peticiones se construirán a partir de esta URL.
    // Ejemplo:
    // GET /api/v1/productos/resumen -> BASE_URL + ese endpoint
    // ----------------------------------------------------------
    private const val BASE_URL = "https://nolimits-backend-final.onrender.com/"

    // ----------------------------------------------------------
    // Interceptor de logging.
    // Permite ver en Logcat TODAS las solicitudes y respuestas
    // HTTP: headers, body, códigos, tiempos, etc.
    //
    // Ideal para debug de API.
    // BODY -> el nivel más detallado
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ----------------------------------------------------------
    // Cliente HTTP de bajo nivel (OkHttp).
    //
    // Aquí configuramos:
    // - cookieJar: necesario para mantener la sesión con el backend.
    // - timeouts: para evitar que la app quede esperando indefinidamente.
    // - logging: para ver las peticiones y respuestas en Logcat.
    //
    // CookieJarImpl() guarda las cookies recibidas del backend
    // (como JSESSIONID), permitiendo que las siguientes peticiones
    // sean reconocidas como "usuario autenticado".
    // ----------------------------------------------------------
    private val client = OkHttpClient.Builder()
        .cookieJar(CookieJarImpl())                             // Mantiene la sesión del backend
        .connectTimeout(30, TimeUnit.SECONDS)   // Tiempo máximo para conectar
        .readTimeout(60, TimeUnit.SECONDS)      // Tiempo máximo para leer respuesta
        .addInterceptor(logging)                    // Log de peticiones/respuestas
        .build()

    // ----------------------------------------------------------
    // Retrofit:
    // Crea la implementación real de NoLimitsApiService.
    //
    // - baseUrl(): URL principal del backend.
    // - client(): usa el OkHttp configurado con cookies, logs y timeouts.
    // - addConverterFactory(): indica cómo convertir JSON -> objetos Kotlin.
    //
    // by lazy -> se crea solo cuando se usa por primera vez.
    // ----------------------------------------------------------
    val service: NoLimitsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoLimitsApiService::class.java)
    }
}