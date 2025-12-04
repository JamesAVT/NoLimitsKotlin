package com.example.nolimits.data.repository

import com.example.nolimits.R
import com.example.nolimits.data.remote.api.ApiClient
import com.example.nolimits.data.remote.dto.ProductoResumenDto
import com.example.nolimits.domain.Product
import com.example.nolimits.ui.mappers.ProductImageMapper
import kotlin.random.Random

class ProductRepository {

    private val api = ApiClient.service

    // ============================
    // LISTAS DE IMÁGENES POR TIPO
    // ============================

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

    private fun randomImage(list: List<Int>): Int {
        return list[Random.Default.nextInt(list.size)]
    }

    // ============================
    // FETCH
    // ============================

    suspend fun fetchProducts(): List<Product> {
        val dtoList: List<ProductoResumenDto> = api.getProductosResumen()

        return dtoList.map { dto ->
            Product(
                id = dto.id.toInt(),
                name = dto.nombre,
                price = dto.precio,
                tipoProducto = dto.tipoProducto,
                imageRes = imageFor(dto)
            )
        }
    }

    // ============================
    // MAPEO DE IMÁGENES
    // ============================

    private fun imageFor(dto: ProductoResumenDto): Int {
        return ProductImageMapper.getImage(dto.id.toInt())
    }
}