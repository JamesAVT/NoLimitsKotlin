package com.example.nolimits

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.example.nolimits.ui.screens.RecuperarContrasenaScreen
import org.junit.Rule
import org.junit.Test

// NOTA: este test NO requiere @OptIn porque PantallaPrincipal no usa ningún
// componente experimental de Material 3 ni Foundation. Todo lo que renderiza
// está en la versión estable de Compose, por lo que no es necesario habilitar
// APIs experimentales.
class RecuperarContrasenaScreenTest {

    // Regla obligatoria para habilitar pruebas de Jetpack Compose
    // createComposeRule() crea un ambiente donde se puede renderizar la UI.
    @get:Rule
    val composeTestRule = createComposeRule()

    // -- Test 1: Verificar textos principales --
    @Test
    fun testRecuperarContrasenaElementosEstaticos() {

        // Renderizamos la pantalla dentro del entorno de prueba.
        // setContent {} crea una "mini Activity" donde Compose puede dibujar la UI
        // temporalmente SOLO para este test.

        // Dentro del bloque definimos:
        // 1) Un NavController básico (rememberNavController())
        //    - Este controlador de navegación NO realiza rutas reales
        //    - Solo evita que Compose falle si la pantalla usa navController.navigate(...)

        // 2) Llamamos directamente al composable que queremos testear:
        //    RecuperarContrasenaScreen()
        // De esta forma, la pantalla queda cargada y lista para que el
        // test interactúe con sus elementos (botones, textos, etc.)
        composeTestRule.setContent {
            val navController = rememberNavController()
            RecuperarContrasenaScreen(navController = navController)
        }

        // Encabezado superior
        composeTestRule.onNodeWithText("°-._ NoLimits _.-°")
            .assertIsDisplayed()

        // Título principal
        composeTestRule.onNodeWithText("Recuperar Contraseña")
            .assertIsDisplayed()

    }

    @Test
    fun testRecuperarContrasenaElementosEstaticos2() {

        // Renderizamos la pantalla dentro del entorno de prueba.
        // setContent {} crea una "mini Activity" donde Compose puede dibujar la UI
        // temporalmente SOLO para este test.

        // Dentro del bloque definimos:
        // 1) Un NavController básico (rememberNavController())
        //    - Este controlador de navegación NO realiza rutas reales
        //    - Solo evita que Compose falle si la pantalla usa navController.navigate(...)

        // 2) Llamamos directamente al composable que queremos testear:
        //    RecuperarContrasenaScreen()
        // De esta forma, la pantalla queda cargada y lista para que el
        // test interactúe con sus elementos (botones, textos, etc.)
        composeTestRule.setContent {
            val navController = rememberNavController()
            RecuperarContrasenaScreen(navController = navController)
        }

        // Texto explicativo (usa substring porque es largo)
        // NOTA IMPORTANTE:
        // El texto "Ingresa tu correo" aparece DOS veces en OutlinedTextField
        // (como label y como nodo editable). Si usamos onNodeWithText() el test falla
        // porque Compose espera 1 solo nodo y encuentra 2.
        // Por eso usamos onAllNodesWithText() y verificamos que la lista NO esté vacía.

        val nodoCorreo = composeTestRule
            .onAllNodesWithText("Ingresa tu correo", substring = true)
            .fetchSemanticsNodes()
        assert(nodoCorreo.isNotEmpty())

        // Footer
        composeTestRule.onNodeWithText("_.-°-._ All in One _.-°-._")
            .assertIsDisplayed()

    }

    // -- Test 2: Verifica campo de correo y botón
    @Test
    fun testRecuperarContrasenaCorreoYBoton() {

        // Renderizamos la pantalla dentro del entorno de prueba.
        // setContent {} crea una "mini Activity" donde Compose puede dibujar la UI
        // temporalmente SOLO para este test.

        // Dentro del bloque definimos:
        // 1) Un NavController básico (rememberNavController())
        //    - Este controlador de navegación NO realiza rutas reales
        //    - Solo evita que Compose falle si la pantalla usa navController.navigate(...)

        // 2) Llamamos directamente al composable que queremos testear:
        //    RecuperarContrasenaScreen()
        // De esta forma, la pantalla queda cargada y lista para que el
        // test interactúe con sus elementos (botones, textos, etc.)
        composeTestRule.setContent {
            val navController = rememberNavController()
            RecuperarContrasenaScreen(navController = navController)
        }

        // Campo texto "Ingresa tu correo"
        // IMPORTANTE:
        // El OutlinedTextField genera dos nodos con este mismo texto:
        // 1) Label del TextField
        // 2) Nodo editable
        // Para evitar el error "Expected at most 1 node", tomamos SOLO el primero
        val correoField = composeTestRule.onNode(
            hasText("Ingresa tu correo", substring = true) and hasSetTextAction()
        )

        correoField.assertIsDisplayed()
        correoField.performTextInput("usuario@gmail.com")

        // Botón "Continuar"
        composeTestRule.onNodeWithText("Continuar")
            .assertIsDisplayed()

        // IMPORTANTE:
        // No usamos .performClick() porque este botón navega hacia otra ruta ("signin").
        // Como el test NO posee un NavGraph configurado, la navegación produciría error.
    }
}