package com.example.nolimits.data.remote

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

// ============================================================================
// CookieJarImpl.kt
// ----------------------------------------------------------------------------
// Implementación personalizada de CookieJar para Retrofit + OkHttp.
//
// ¿Para qué sirve?
// ----------------
// Este "cookie jar" es responsable de guardar y reenviar cookies entre llamadas
// HTTP. Es NECESARIO cuando nuestro backend maneja sesiones con cookies.
//
// CookieJarImpl almacena las cookies de cada dominio y las vuelve a enviar
// automáticamente en próximas llamadas.
// ============================================================================
class CookieJarImpl : CookieJar {

    // ------------------------------------------------------------------------
    // cookieStore:
    // Un mapa donde la llave es el HOST (ej: "nolimits-backend-final.onrender.com")
    // y el valor es la lista de cookies asociadas a ese dominio.
    //
    // ------------------------------------------------------------------------
    private val cookieStore = HashMap<String, List<Cookie>>()

    // ------------------------------------------------------------------------
    // saveFromResponse()
    //
    // Se ejecuta cada vez que el servidor RESPONDE con cookies.
    // Guarda las cookies para poder reutilizarlas en próximas requests.
    //
    // Ejemplo real:
    // - Usuario inicia sesión
    // - Backend responde Set-Cookie
    // - Guardamos esa cookie aquí.
    // ------------------------------------------------------------------------
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }

    // ------------------------------------------------------------------------
    // loadForRequest()
    //
    // Antes de enviar una request, OkHttp pregunta:
    //     "¿Tengo cookies guardadas para este dominio?"
    //
    // Si existen, las adjunta automáticamente al encabezado:
    //     Cookie: JSESSIONID=abc123
    //
    // Esto mantiene la sesión activa en cada call.
    // ------------------------------------------------------------------------
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }
}