package com.example.nolimits

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.example.nolimits.domain.Product
import com.example.nolimits.ui.screens.BoletaComprobante
import org.junit.Rule
import org.junit.Test

// Clase de tests de la pantalla BoletaComprobante

// Este test necesita @OptIn(ExperimentalMaterial3Api::class) porque la pantalla
// BoletaComprobante utiliza varios componentes de Material 3 que aún están
// marcados como experimentales, tales como CenterAlignedTopAppBar,
// BottomAppBar y CardDefaults.
@OptIn(ExperimentalMaterial3Api::class)
class BoletaComprobanteTest {

    // Regla obligatoria para habilitar pruebas de Jetpack Compose
    // createComposeRule() crea un ambiente donde se puede renderizar la UI.
    @get:Rule
    val composeTestRule = createComposeRule()

    // Test de la pantalla del comprobante
    @Test
    fun testElementosEstaticosBoleta() {

        // -- Datos falsos --
        // Aquí creamos dos productos ficticios para simular una compra.
        val productos = listOf(
            Product(id = 1, name = "Producto A", price = 5000.0, imageRes = null),
            Product(id = 2, name = "Producto B", price = 10000.0, imageRes = null)
        )

        // Renderizamos la pantalla dentro del entorno de prueba.
        composeTestRule.setContent {
            // Se crea un NavController para que la pantalla no falle.
            // Esto no navega, solo permite que la pantalla cargue.
            val navController = rememberNavController().apply {
                // Se agrega el navegador básico para que Compose pueda manejar rutas.
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            // Aquí llamamos directamente al Composable que queremos testear.
            BoletaComprobante(
                navController = navController,
                itemsComprados = productos, // Lista ficticia
                total = 15000.0, // Total ficticio
                ultimos4 = "1234" // Últimos dígitos de una tarjeta ficticia
            )
        }

        // -- Verifica textos principales --

        // Verifica que el título principal exista en la pantalla.
        composeTestRule.onNodeWithText("🧾 Comprobante de Compra")
            .assertIsDisplayed()

        // Verifica que el subtítulo exista.
        composeTestRule.onNodeWithText("Simulación de compra")
            .assertIsDisplayed()

        // Verifica que los productos ficticios aparezcan.
        composeTestRule.onNodeWithText("Producto A")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Producto B")
            .assertIsDisplayed()

        // Verifica que aparezca el total.
        composeTestRule.onNodeWithText("Total:", substring = true)
            .assertExists()

        // Verifica que aparezca el método de pago con los últimos dígitos.
        composeTestRule.onNodeWithText("Método de pago: Tarjeta **** 1234")
            .assertIsDisplayed()

        // Verifica que el botón "Volver a NoLimits" esté visible.
        // Aquí no usamos .performClick() porque este botón llama a
        // navController.navigate(...). EN las pruebas, el NavController no tiene
        // NavGraph (estructura que define todas las pantallas como destinos o "routes" y las conexiones entre ellas dentro de una app Jetpack Compose),
        // por lo que genera error.
        composeTestRule.onNodeWithText("Volver a NoLimits")
            .assertIsDisplayed()
    }

    @Test
    fun testElementosEstaticosBoleta2() {

        // -- Datos falsos --
        // Aquí creamos dos productos ficticios para simular una compra.
        val productos = listOf(
            Product(id = 1, name = "Producto A", price = 5000.0, imageRes = null),
            Product(id = 2, name = "Producto B", price = 10000.0, imageRes = null)
        )

        // Renderizamos la pantalla dentro del entorno de prueba.
        composeTestRule.setContent {
            // Se crea un NavController para que la pantalla no falle.
            // Esto no navega, solo permite que la pantalla cargue.
            val navController = rememberNavController().apply {
                // Se agrega el navegador básico para que Compose pueda manejar rutas.
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            // Aquí llamamos directamente al Composable que queremos testear.
            BoletaComprobante(
                navController = navController,
                itemsComprados = productos, // Lista ficticia
                total = 15000.0, // Total ficticio
                ultimos4 = "1234" // Últimos dígitos de una tarjeta ficticia
            )
        }

        // -- Verifica textos principales --


        // Verifica que el subtítulo exista.
        composeTestRule.onNodeWithText("Simulación de compra")
            .assertIsDisplayed()


    }

}