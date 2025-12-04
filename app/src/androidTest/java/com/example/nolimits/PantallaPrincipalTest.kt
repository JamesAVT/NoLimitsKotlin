package com.example.nolimits

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.nolimits.ui.screens.PantallaPrincipal
import org.junit.Rule
import org.junit.Test

// NOTA: este test NO requiere @OptIn porque PantallaPrincipal no usa ningún
// componente experimental de Material 3 ni Foundation. Todo lo que renderiza
// está en la versión estable de Compose, por lo que no es necesario habilitar
// APIs experimentales.
class PantallaPrincipalTest {

    // Regla obligatoria para habilitar pruebas de Jetpack Compose
    // createComposeRule() crea un ambiente donde se puede renderizar la UI.
    @get:Rule
    val composeTestRule = createComposeRule()


    // ========================================================
    // TEST 1 — Verificar elementos estáticos de la pantalla
    // ========================================================
    @Test
    fun testPantallaPrincipalElementosEstaticos() {

        // Renderizamos la pantalla principal dentro del entorno de test
        composeTestRule.setContent {

            // NavController simple:
            // En esta pantalla NO se hacen navegaciones,
            // así que no es necesario agregar ComposeNavigator.
            val navController = rememberNavController()

            PantallaPrincipal(navController = navController)
        }

        // --- Encabezado superior ---
        composeTestRule.onNodeWithText("°-._ NoLimits _.-°")
            .assertIsDisplayed()

        // --- Tarjetas informativas ---
        composeTestRule.onNodeWithText("¿Quiénes somos?")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Sucursales")
            .assertIsDisplayed()
    }

    @Test
    fun testPantallaPrincipalElementosEstaticos2() {

        // Renderizamos la pantalla principal dentro del entorno de test
        composeTestRule.setContent {

            // NavController simple:
            // En esta pantalla NO se hacen navegaciones,
            // así que no es necesario agregar ComposeNavigator.
            val navController = rememberNavController()

            PantallaPrincipal(navController = navController)
        }

        composeTestRule.onNodeWithText("Soporte")
            .assertIsDisplayed()

        // --- Footer inferior ---
        composeTestRule.onNodeWithText("_.-°-._ All in One _.-°-._")
            .assertIsDisplayed()
    }


    // ========================================================
    // TEST 2 — Verificar visibilidad de los botones principales
    // ========================================================
    @Test
    fun testPantallaPrincipalBotones() {

        composeTestRule.setContent {

            // NavController simple:
            // En esta pantalla NO se hacen navegaciones,
            // así que no es necesario agregar ComposeNavigator.
            val navController = rememberNavController()

            PantallaPrincipal(navController = navController)
        }

        // --- Botón "Iniciar sesión" ---
        composeTestRule.onNodeWithText("Iniciar sesión")
            .assertIsDisplayed()

        // --- Botón "Crear cuenta" ---
        composeTestRule.onNodeWithText("Crear cuenta")
            .assertIsDisplayed()

        // ------------------------------------------------------------
        // IMPORTANTE:
        // No se usa .performClick() porque esta pantalla navega
        // a rutas definidas en un NavGraph real.
        //
        // Como los tests unitarios NO tienen un NavHost configurado,
        // un click produciría error: “Destination not found”.
        //
        // Por eso solo se verifica que los botones EXISTAN y sean visibles.
        // ------------------------------------------------------------
    }
}