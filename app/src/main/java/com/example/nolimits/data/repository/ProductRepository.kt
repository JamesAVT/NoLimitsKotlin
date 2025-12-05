package com.example.nolimits.data.repository

import com.example.nolimits.R
import com.example.nolimits.data.remote.api.ApiClient
import com.example.nolimits.data.remote.dto.ProductoResumenDto
import com.example.nolimits.domain.Product
import com.example.nolimits.ui.mappers.ProductImageMapper
import kotlin.random.Random

// ============================================================================
// ProductRepository.kt
// ----------------------------------------------------------------------------
// Esta clase es el repositorio de productos. Su responsabilidad es:
//
//   1) Obtener la lista de productos desde el backend (API REST).
//   2) Convertir los DTOs (datos crudos del backend) en objetos Product
//      que la app realmente usa en la UI.
//   3) Asignar una imagen local (drawable) a cada producto según su tipo,
//      de forma automática.
//
// El ViewModel (CatalogViewModel) nunca debería llamar a Retrofit directamente.
// Siempre utiliza este repositorio como intermediario.
// ===========================================================================
class ProductRepository {

    // Servicio Retrofit generado en ApiClient
    private val api = ApiClient.service

    // =========================================================================
    // LISTAS DE IMÁGENES
    // -------------------------------------------------------------------------
    // Como el backend NO envía imágenes, la app asigna imágenes locales
    // dependiendo del tipo de producto.
    //
    // Cada lista contiene varias imágenes. Luego se elige una al azar para
    // darle variedad visual al catálogo.
    // =========================================================================

    private val videogameImages = listOf(
        R.drawable.vgspiderman1,
        R.drawable.vgspiderman2,
        R.drawable.vgspidermanmm
    )

    private val movieImages = listOf(
        R.drawable.pspiderman1,
        R.drawable.pspiderman2,
        R.drawable.pspiderman3
    )

    private val accessoryImages = listOf(
        R.drawable.accspiderman1,
        R.drawable.accspiderman2,
        R.drawable.accspiderman3
    )

    // Función que devuelve una imagen aleatoria de la lista entregada
    private fun randomImage(list: List<Int>): Int {
        return list[Random.Default.nextInt(list.size)]
    }

    // =========================================================================
    // FETCH — Obtener productos desde el backend
    // -------------------------------------------------------------------------
    // Esta función:
    //   1. Llama a la API para obtener productos (DTO crudos).
    //   2. Convierte cada DTO -> Product (modelo usado en la app).
    //   3. Asigna una imagen local usando imageFor().
    //
    // return:
    //   Lista de productos listos para mostrarse en Compose.
    // =========================================================================
    suspend fun fetchProducts(): List<Product> {
        // Llamada Retrofit → devuelve lista de ProductoResumenDto (DTO del backend)
        val dtoList: List<ProductoResumenDto> = api.getProductosResumen()

        // Convertimos cada DTO en Product
        return dtoList.map { dto ->
            Product(
                id = dto.id.toInt(),
                name = dto.nombre,
                price = dto.precio,
                tipoProducto = dto.tipoProducto,
                imageRes = imageFor(dto)    // asignar imagen automática por tipo
            )
        }
    }

    // =========================================================================
    // MAPEO DE IMÁGENES SEGÚN TIPO DE PRODUCTO
    // -------------------------------------------------------------------------
    // Delegamos en ProductImageMapper, que asigna una imagen FIJA por ID.
    //
    // Ventaja:
    //     → Los productos siempre tendrán la misma imagen cada vez que carguemos
    //       la app (no cambia aleatoriamente).
    //     → Es ideal para evitar que las imágenes "bailen" o cambien de orden.
    //
    // =========================================================================
    private fun imageFor(dto: ProductoResumenDto): Int {
        return ProductImageMapper.getImage(dto.id.toInt())
    }
}