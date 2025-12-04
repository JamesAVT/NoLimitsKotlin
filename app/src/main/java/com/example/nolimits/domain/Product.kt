// Product.kt
// Modelo con atributos.

package com.example.nolimits.domain

import androidx.annotation.DrawableRes

/*
    Modelo con atributos.
*/

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val tipoProducto: String,
    @DrawableRes val imageRes: Int? = null
)