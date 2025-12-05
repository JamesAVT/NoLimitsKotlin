// Ruta: app/src/main/java/com/example/nolimits/data/remote/api/NoLimitsApiService.kt
package com.example.nolimits.data.remote.api

import com.example.nolimits.data.remote.dto.LoginRequest
import com.example.nolimits.data.remote.dto.ProductoResumenDto
import com.example.nolimits.data.remote.dto.UsuarioRegisterRequest
import com.example.nolimits.data.remote.dto.VentaRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// =====================================================================
// NoLimitsApiService.kt
// ---------------------------------------------------------------------
// Esta interfaz define TODOS los endpoints que la app Android puede
// llamar en el backend.
//
// Retrofit genera automáticamente las funciones reales que hacen las
// peticiones HTTP, gracias a las anotaciones (@POST, @GET, @Body, etc.).
//
// Cada función:
//   - corresponde a un endpoint del backend
//   - recibe parámetros (body, path, query…)
//   - devuelve un Response<T> con el resultado
//
// Todas las funciones son "suspend" -> se ejecutan dentro de corrutinas.
// =====================================================================
interface NoLimitsApiService {

    // Productos (resumen)
    @GET("api/v1/productos/resumen")
    suspend fun getProductosResumen(): List<ProductoResumenDto>

    // Login (solo validación de credenciales)
    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<ResponseBody>

    // Envia un POST al backend para crear un nuevo usuario.
    @POST("api/v1/usuarios")
    suspend fun registerUser(
        @Body body: UsuarioRegisterRequest
    ): Response<ResponseBody>

    // REGISTRAR UNA VENTA
    @POST("api/v1/ventas/registrar")
    suspend fun registrarVenta(
        @Body body: VentaRequest
    ): Response<ResponseBody>

    // BUSCAR USUARIO POR CORREO
    @GET("api/v1/usuarios/correo/{correo}")
    suspend fun getUsuarioPorCorreo(
        @Path("correo") correo: String
    ): Response<ResponseBody>
}