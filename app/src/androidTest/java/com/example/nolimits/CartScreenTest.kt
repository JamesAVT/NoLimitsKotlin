package com.example.nolimits

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.example.nolimits.domain.Product
import com.example.nolimits.ui.screens.CartScreen
import com.example.nolimits.ui.viewmodels.CartViewModel
import org.junit.Rule
import org.junit.Test


// Este test necesita @OptIn(ExperimentalMaterial3Api::class) porque la pantalla
// CartScreen utiliza componentes de Material 3 experimentales, incluyendo
// CenterAlignedTopAppBar y BottomAppBar.
@OptIn(ExperimentalMaterial3Api::class)
class CartScreenTest {

    // Regla para habilitar Compose en los tests.
    // Crea un entorno donde se puede renderizar la UI.
    @get:Rule
    val composeTestRule = createComposeRule()

    // -- TEST CARRITO VACÍO --
    @Test
    fun testCarritoVacio() {

        // Se crea un ViewModel vacío (sin productos en el carrito)
        val cartViewModel = CartViewModel()

        // Renderiza la pantalla dentro del entorno de prueba
        composeTestRule.setContent {

            // NavController para que la pantalla no falle.
            // No navega, solo permite que Compose cargue bien la UI.
            val navController = rememberNavController().apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            // Llamamos al Composable a testear
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        // Verifica que el título principal del carrito aparezca.
        composeTestRule.onNodeWithText("🛒 Carrito de compras")
            .assertIsDisplayed()

        // Verifica que aparezca el mensaje de carrito vacío.
        composeTestRule.onNodeWithText("Tu carrito está vacío!")
            .assertIsDisplayed()
    }

    // -- TEST CARRITO CON PRODUCTOS --

    @Test
    fun testCarritoConProductos() {

        // Creamos un ViewModel y le agregamos un producto ficticio
        val cartViewModel = CartViewModel()

        // Renderizamos la pantalla
        composeTestRule.setContent {

            // NavController para cargar correctamente la pantalla
            val navController = rememberNavController().apply {
                // ComposeNavigator es obligatorio en tests unitarios porque,
                // a diferencia de una Activity real, aquí no existe un NavHost.
                // Agregar este navigator evita errores de navegación.
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            // Renderizamos la pantalla del carrito dentro del test.
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel // ViewModel simulado para el test
            )
        }

        // runOnUiThread es necesario porque los cambios de estado del ViewModel
        // (como modificar LiveData o StateFlow) *deben* ejecutarse en el hilo UI.
        // Si no se hace así, los tests pueden fallar por threading issues.
        composeTestRule.runOnUiThread {
            cartViewModel.addToCart(
                Product(
                    id = 1,
                    name = "Producto Test",
                    price = 5000.0,
                    tipoProducto = "Videojuego",
                    imageRes = null
                )
            )
        }

        composeTestRule.waitForIdle()

        // Verifica que aparezca el producto agregado
        composeTestRule.onNodeWithText("Producto Test", substring = true)
            .assertIsDisplayed()

        // Verifica que el botón "Eliminar" exista y funcione
        // El "Ir a método de pago" navega y en tests no hay NavGraph, por lo que fallaría.
        // "Eliminar" dejaría el carrito vacío y Compose ocultaría el botón de pago, haciendo
        // fallar el test. Solo se verifica que exista.
        composeTestRule.onNodeWithText("Eliminar")
            .assertIsDisplayed() // Exista

        // Verifica el botón ir a método de pago.
        // No usamos .performClick() porque este botón navega y en los tests
        // el NavController no tiene NavGraph (estructura que define todas las pantallas como destinos o "routes" y las conexiones entre ellas dentro de una app Jetpack Compose),
        // por lo que causaría error.
        composeTestRule.onNodeWithText("Ir a método de pago", substring = true)
            .assertIsDisplayed()
    }
}