package com.example.nolimits

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.example.nolimits.domain.Product
import com.example.nolimits.ui.screens.CatalogScreen
import com.example.nolimits.ui.viewmodels.CartViewModel
import org.junit.Rule
import org.junit.Test

// Este test necesita @OptIn porque CatalogScreen utiliza:
// - stickyHeader() -> API experimental de Foundation (ExperimentalFoundationApi)
// - CenterAlignedTopAppBar y BottomAppBar -> APIs experimentales de Material 3
// Por lo tanto, debemos habilitar explícitamente ambas anotaciones.
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
class CatalogScreenTest {

    // Regla obligatoria para habilitar pruebas de Jetpack Compose
    // createComposeRule() crea un ambiente donde se puede renderizar la UI.
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCatalogoElementosEstaticos() {

        // Renderizamos la pantalla dentro del entorno de prueba.
        composeTestRule.setContent {

            // NavController para evitar errores
            val navController = rememberNavController().apply {
                // ComposeNavigator es obligatorio en tests unitarios porque,
                // a diferencia de una Activity real, aquí no existe un NavHost.
                // Agregar este navigator evita errores de navegación.
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            // Renderizamos la pantalla del catálogo dentro del entorno de prueba.
            CatalogScreen(
                navController = navController,
                // ViewModel del carrito:
                // En este test usamos un CartViewModel vacío porque
                // solo nos interesa que la pantalla cargue correctamente.
                cartViewModel = CartViewModel()
            )
        }

        // Esperamos a que Compose termine de construir la UI
        composeTestRule.waitForIdle()

        // -- PRUEBAS DE ELEMENTOS --

        // Verifica que el título de la app exista.
        // substring = true permite encontrar el texto aunque no coincida exactamente.
        // Útil cuando el texto contiene símbolos, tildes o espacios extra.
        composeTestRule.onNodeWithText("°-._ NoLimits _.-°", substring = true)
            .assertIsDisplayed()

    }

    @Test
    fun testCatalogoElementosEstaticos2() {

        // Renderizamos la pantalla dentro del entorno de prueba.
        composeTestRule.setContent {

            // NavController para evitar errores
            val navController = rememberNavController().apply {
                // ComposeNavigator es obligatorio en tests unitarios porque,
                // a diferencia de una Activity real, aquí no existe un NavHost.
                // Agregar este navigator evita errores de navegación.
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            // Renderizamos la pantalla del catálogo dentro del entorno de prueba.
            CatalogScreen(
                navController = navController,
                // ViewModel del carrito:
                // En este test usamos un CartViewModel vacío porque
                // solo nos interesa que la pantalla cargue correctamente.
                cartViewModel = CartViewModel()
            )
        }
        // -- PRUEBAS DE ELEMENTOS --
        //  Footer
        composeTestRule.onNodeWithText("All in One", substring = true)
            .assertIsDisplayed()

        // Verifica que el carro esté visible.
        composeTestRule.onNodeWithText("🛒")
            .assertIsDisplayed()

        // Verifica que el slogan del catálogo sea visible
        // Usamos substring = true porque el texto real incluye saltos de línea
        // y podría no coincidir exactamente. Esto permite que la búsqueda sea parcial.
        composeTestRule.onNodeWithText("_.- Variedad, estilo y calidad en un solo lugar -._", substring = true)
            .assertIsDisplayed()
    }
}