package com.example.nolimits

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.nolimits.ui.screens.ResumenScreen
import com.example.nolimits.ui.viewmodels.UsuarioViewModel
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para la pantalla resumen del registro (ResumenScreen).
 *
 * Esta pantalla NO requiere @OptIn en los tests porque:
 * - No usa componentes experimentales dentro del test.
 * - Solo se renderiza un Column con textos estáticos generados desde el ViewModel.
 *
 * El objetivo de estos tests es verificar que:
 * 1) Los datos cargados en el UsuarioViewModel se reflejan correctamente.
 * 2) El estado “acepta términos” se muestre correctamente como “Aceptados” o “No aceptados”.
 */

class ResumenScreenTest {

    // Regla obligatoria para habilitar pruebas de Jetpack Compose
    // createComposeRule() crea un ambiente donde se puede renderizar la UI.
    @get:Rule
    val composeTestRule = createComposeRule()

    // ViewModel real (no es necesario un fake porque ResumenScreen solo lee valores)
    private val usuarioVM = UsuarioViewModel()


    // Test 1: Verificar elementos estáticos cuando el usuario SÍ acepta términos
    @Test
    fun testResumenElementosEstaticos() {

        // Preparamos datos simulados antes de renderizar la pantalla
        usuarioVM.onNombreChange("Martin")
        usuarioVM.onCorreoChange("martingarrix@gmail.com")
        usuarioVM.onDireccionChange("Av. Amsterdam 123")
        usuarioVM.onClaveChange("123456789101")
        usuarioVM.onAceptarTerminosChange(true)

        // Renderizamos la pantalla
        composeTestRule.setContent {
            ResumenScreen(viewModel = usuarioVM)
        }

        // Título
        composeTestRule.onNodeWithText("Resumen del Registro")
            .assertIsDisplayed()

        // Verificamos que cada fila muestre los datos correctos del ViewModel
        composeTestRule.onNodeWithText("Nombre: Martin")
            .assertIsDisplayed()

        // Correo
        composeTestRule.onNodeWithText("Correo: martingarrix@gmail.com")
            .assertIsDisplayed()

        // Dirección
        composeTestRule.onNodeWithText("Dirección: Av. Amsterdam 123")
            .assertIsDisplayed()

        /**
         * La contraseña no se muestra directamente,
         * sino como un número de asteriscos equivalente a su longitud.
         * En este caso "123456789101" -> 12 caracteres -> "************"
         */
        // Contraseña oculta
        composeTestRule.onNodeWithText("Contraseña: ************")
            .assertIsDisplayed()

        // Términos
        composeTestRule.onNodeWithText("Términos: Aceptados")
            .assertIsDisplayed()

    }

    // Test 2: Verificar que muestre correctamente cuando NO acepta términos
    @Test
    fun testResumenTerminosNoAceptados() {

        // Cambiamos solo este valor
        usuarioVM.onAceptarTerminosChange(false)

        composeTestRule.setContent {
            ResumenScreen(viewModel = usuarioVM)
        }

        // Verificamos la salida correspondiente
        composeTestRule.onNodeWithText("Términos: No aceptados")
            .assertIsDisplayed()
    }
}