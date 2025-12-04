package com.example.nolimits

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.example.nolimits.ui.screens.PantallaMetodoPago
import com.example.nolimits.ui.viewmodels.CartViewModel
import org.junit.Rule
import org.junit.Test

// Este test necesita @OptIn(ExperimentalMaterial3Api::class) porque
// PantallaMetodoPago usa varios componentes Material 3 experimentales, como
// CenterAlignedTopAppBar, BottomAppBar y OutlinedTextField.
@OptIn(ExperimentalMaterial3Api::class)
class MetodoPagoScreenTest {

    // Regla obligatoria para habilitar pruebas de Jetpack Compose
    // createComposeRule() crea un ambiente donde se puede renderizar la UI.
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testElementosEstaticosYBotonDeshabilitado() {

        // Renderizamos la pantalla
        composeTestRule.setContent {

            // NavController sin NavGraph (solo para evitar errores)
            val navController = rememberNavController().apply {
                // Agregar ComposeNavigator evita errores al llamar `navigate()`
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            // Cargamos la pantalla que se quiere probar
            PantallaMetodoPago(
                navController = navController,
                // ViewModel del carríto vacío, ya que solo necesitamos que exista.
                cartViewModel = CartViewModel()
            )
        }

        // -- VERIFICAR TEXTOS --
        composeTestRule.onNodeWithText("°-._ NoLimits _.-°", substring = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Total:", substring = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Nombre del titular")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Número de tarjeta", substring = true)
            .assertIsDisplayed()

    }

    @Test
    fun testElementosEstaticosYBotonDeshabilitado2() {

        // Renderizamos la pantalla
        composeTestRule.setContent {

            // NavController sin NavGraph (solo para evitar errores)
            val navController = rememberNavController().apply {
                // Agregar ComposeNavigator evita errores al llamar `navigate()`
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            // Cargamos la pantalla que se quiere probar
            PantallaMetodoPago(
                navController = navController,
                // ViewModel del carríto vacío, ya que solo necesitamos que exista.
                cartViewModel = CartViewModel()
            )
        }

        // -- VERIFICAR TEXTOS --
        composeTestRule.onNodeWithText("Vencimiento", substring = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("CVV")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Guardar tarjeta", substring = true)
            .assertIsDisplayed()

        // Botón para confirmar DEBE ESTAR DESHABILITADO!!
        composeTestRule.onNodeWithText("Confirmar y pagar")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun testBotonSeHabilitaConFormularioValido() {

        // Renderizamos la pantalla
        composeTestRule.setContent {

            // NavController sin NavGraph (solo para evitar errores)
            val navController = rememberNavController().apply {
                // Agregar ComposeNavigator evita errores al llamar `navigate()`
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            // Cargamos la pantalla que se quiere probar
            PantallaMetodoPago(
                navController = navController,
                // ViewModel del carríto vacío, ya que solo necesitamos que exista.
                cartViewModel = CartViewModel()
            )
        }

        // NOMBRE
        composeTestRule.onNodeWithText("Nombre del titular")
            // performTextInput() permite escribir texto en un nodo que sea un campo
            // editable. Simula que el usuario está escribiendo en la pantalla durante la prueba.
            .performTextInput("Martin Garrix")

        // NUMERO TARJETA (16 dígitos)
        // substring = true permite buscar texto parcial dentro de un nodo (elemento de la interfaz que Compose puede encontrar y testear).
        // Ejemplo: "Número de tarjeta (16 dígitos)" contiene la frase "Número de tarjeta",
        // entonces substring=true evita tener que escribir el texto completo.
        composeTestRule.onNodeWithText("Número de tarjeta", substring = true)
            .performTextInput("1234567812345678")

        // VENCIMIENTO
        // substring = true permite encontrar el nodo (elemento de la interfaz que Compose puede encontrar y testear) aunque el texto completo
        // del label sea más largo, por ejemplo "Vencimiento (MM/AA)".
        // Buscamos solo "Vencimiento" para hacer el test más estable.

        composeTestRule.onNodeWithText("Vencimiento", substring = true)
            .performTextInput("1128") // Se auto formatea a 11/28

        // CVV
        composeTestRule.onNodeWithText("CVV")
            .performTextInput("123")

        // -- Ahora el botón debe ESTAR HABILITADO --
        // IMPORTANTE:
        // No se usa performClick() sobre el botón "Confirmar y pagar"
        // porque esta acción dispara navegación hacia la pantalla "boleta",
        // y al no existir un NavGraph real dentro del test, el click generaría
        // un error. Solo verificamos que el botón esté habilitado/deshabilitado.
        composeTestRule.onNodeWithText("Confirmar y pagar")
            .assertIsEnabled()
    }
}