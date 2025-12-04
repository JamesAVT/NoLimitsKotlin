package com.example.nolimits.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.nolimits.data.datastore.EstadoDataStore
import com.example.nolimits.data.remote.api.ApiClient
import com.example.nolimits.data.remote.dto.DetalleVentaRequest
import com.example.nolimits.data.remote.dto.VentaRequest
import com.example.nolimits.domain.Product
import kotlinx.coroutines.flow.first

/*
    Controla las funciones del carrito.
    Agregar producto.
    Remover producto.
    Calcular total.
    Guardar carrito en DataStore si quieres persistencia.
*/

// ViewModel encargado de manejar la lógica del carrito de compras
class CartViewModel : ViewModel() {

    private val _cartItems = mutableStateListOf<Product>()
    val cartItems: List<Product> get() = _cartItems

    fun addToCart(product: Product) { _cartItems.add(product) }
    fun removeFromCart(product: Product) { _cartItems.remove(product) }
    fun getTotal(): Double = _cartItems.sumOf { it.price }
    fun clearCart() { _cartItems.clear() }

    // -----------------------------------------------------------
    // DETECTAR TIPO DE ENVÍO AUTOMÁTICAMENTE
    // -----------------------------------------------------------
    private fun obtenerMetodoEnvio(producto: Product): Int {

        val tipo = producto.tipoProducto.lowercase()

        return when {
            tipo.contains("juego") -> 34         // Juegos físicos convertidos a digital en tu lógica
            tipo.contains("película") -> 34      // Películas digitales
            else -> 1                                   // Accesorios → Retiro en tienda
        }
    }

    // -----------------------------------------------------------
    // REGISTRAR VENTA
    // -----------------------------------------------------------
    suspend fun registrarVentaEnSwagger(dataStore: EstadoDataStore): Boolean {
        return try {
            val usuarioId = dataStore.obtenerUsuarioId().first()
            if (usuarioId == -1) return false

            if (cartItems.isEmpty()) return false

            val primerProducto = cartItems.first()
            val metodoEnvioId = obtenerMetodoEnvio(primerProducto)

            val detalles = cartItems.map {
                DetalleVentaRequest(
                    productoId = it.id,
                    cantidad = 1,
                    precioUnitario = it.price.toInt()
                )
            }

            val venta = VentaRequest(
                usuarioId = usuarioId,
                metodoPagoId = 1,
                metodoEnvioId = metodoEnvioId,
                estadoId = 1,
                detalles = detalles
            )

            val response = ApiClient.service.registrarVenta(venta)

            response.isSuccessful

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

