package com.example.nolimits.ui.mappers

import com.example.nolimits.R

object ProductImageMapper {

    val images = mapOf(
        // Películas
        57 to R.drawable.pspiderman1,
        58 to R.drawable.pspiderman2,
        59 to R.drawable.pspiderman3,

        // Videojuegos
        1 to R.drawable.vgspiderman1,
        61 to R.drawable.vgspiderman2,
        60 to R.drawable.vgspidermanmm,

        // Accesorios
        63 to R.drawable.accspiderman1,
        64 to R.drawable.accspiderman2,
        65 to R.drawable.accspiderman3
    )

    fun getImage(id: Int): Int {
        return images[id] ?: R.drawable.nolimitslogo
    }
}