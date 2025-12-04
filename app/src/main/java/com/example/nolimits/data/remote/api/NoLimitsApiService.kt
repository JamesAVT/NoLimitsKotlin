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

interface NoLimitsApiService {

    // Productos (resumen)
    @GET("api/v1/productos/resumen")
    suspend fun getProductosResumen(): List<ProductoResumenDto>

    // Login (solo validación de credenciales)
    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<ResponseBody>

    @POST("api/v1/usuarios")
    suspend fun registerUser(
        @Body body: UsuarioRegisterRequest
    ): Response<ResponseBody>

    @POST("api/v1/ventas/registrar")
    suspend fun registrarVenta(
        @Body body: VentaRequest
    ): Response<ResponseBody>
}