package com.example.nolimits

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.example.nolimits.data.local.dao.AppUserDao
import com.example.nolimits.data.local.model.AppUser
import com.example.nolimits.ui.screens.SignInScreen
import com.example.nolimits.ui.viewmodels.AuthViewModel
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para la pantalla de inicio de sesión (SignInScreen).
 *
 * Esta pantalla requiere un ViewModel de autenticación, pero en estos tests
 * usamos un DAO falso (FakeDao) para evitar acceso real a Room y así mantener
 * las pruebas aisladas.
 *
 * IMPORTANTE:
 * - Aquí tampoco se usa @OptIn porque el test NO llama directamente a componentes
 *   experimentales. El @OptIn existe dentro del Composable, pero no afecta al test.
 */
class SignInScreenTest {

    // Regla obligatoria para habilitar pruebas de Jetpack Compose
    // createComposeRule() crea un ambiente donde se puede renderizar la UI.
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * DAO falso para simular el acceso a base de datos.
     *
     * Implementa AppUserDao pero:
     * - Nunca devuelve usuarios reales.
     * - No inserta nada.
     * - login() siempre retorna null -> así evitamos navegación real.
     */
    class FakeDao : AppUserDao {
        override suspend fun getUserByCorreo(correo: String): AppUser? = null
        override suspend fun insertUser(user: AppUser) {}
        override suspend fun login(correo: String, clave: String): AppUser? = null
    }

    // ViewModel que usa el DAO falso
    private val authVM = AuthViewModel(FakeDao())

    // Test 1: Verificar elementos estáticos del Login y que existan

    @Test
    fun testSignInElementosEstaticos() {

        composeTestRule.setContent {
            SignInScreen(
                navController = rememberNavController(),
                authViewModel = authVM
            )
        }

        // Título NAV
        composeTestRule.onNodeWithText("°-._ NoLimits _.-°")
            .assertIsDisplayed()

        // Título de la pantalla
        /**
         * "Iniciar Sesión" aparece dos veces:
         *  1) Como título de la pantalla.
         *  2) Como texto del botón de login.
         *
         * Por eso usamos onAllNodesWithText() y tomamos cada índice.
         */
        composeTestRule.onAllNodesWithText("Iniciar Sesión")[0]
            .assertIsDisplayed()

        // Campos principales
        composeTestRule.onNodeWithText("Email")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Contraseña")
            .assertIsDisplayed()

        // Enlaces
        composeTestRule.onNodeWithText("Olvidé mi contraseña")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Regístrate")
            .assertIsDisplayed()

        // Botón de login
        composeTestRule.onAllNodesWithText("Iniciar Sesión")[1]
            .assertIsDisplayed()

        // Footer
        composeTestRule.onNodeWithText("_.-°-._ All in One _.-°-._")
            .assertIsDisplayed()
    }

    // Test 2: Probar ingreso de datos (inputs)

    @Test
    fun testSignInInputs() {

        composeTestRule.setContent {
            SignInScreen(
                navController = rememberNavController(),
                authViewModel = authVM
            )
        }

        // Ingresar un email
        composeTestRule.onAllNodesWithText("Email")[0]
            .performTextInput("martingarrix@gmail.com")

        // Ingresar una contraseña
        composeTestRule.onAllNodesWithText("Contraseña")[0]
            .performTextInput("123456789101")

        // Verificar que el texto fue ingresado
        composeTestRule.onNodeWithText("martingarrix@gmail.com")
            .assertIsDisplayed()

        /**
         * La contraseña NO se muestra de forma visible porque usa PasswordVisualTransformation().
         *
         * Por lo tanto:
         *  - NO se puede verificar por texto.
         *  - Solo verificamos que el nodo del TextField existe.
         */
        composeTestRule.onAllNodesWithText("Contraseña")[0]
            .assertExists()
    }
}