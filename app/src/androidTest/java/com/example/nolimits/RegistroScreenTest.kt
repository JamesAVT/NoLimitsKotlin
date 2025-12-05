package com.example.nolimits

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.example.nolimits.data.local.dao.AppUserDao
import com.example.nolimits.data.local.model.AppUser
import com.example.nolimits.ui.screens.RegistroScreen
import com.example.nolimits.ui.viewmodels.AuthViewModel
import com.example.nolimits.ui.viewmodels.UsuarioViewModel
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test


/**
 * Pruebas unitarias para la pantalla de Registro (RegistroScreen).
 *
 * IMPORTANTE:
 * Este test NO necesita usar @OptIn(ExperimentalMaterial3Api::class)
 * porque aquí NO estamos utilizando directamente componentes experimentales
 * como LazyColumn, LazyGrid, DatePicker, etc.
 *
 * RegistroScreen SÍ usa APIs experimentales internamente, pero el test
 * solo renderiza la pantalla, no usa dichas APIs directamente.
 * Por eso no es necesario repetir el @OptIn en esta clase.
 */

class RegistroScreenTest {

    // Regla obligatoria para habilitar pruebas de Jetpack Compose
    // createComposeRule() crea un ambiente donde se puede renderizar la UI.
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * DAO falso para pruebas.
     * Esto permite usar AuthViewModel sin acceder a una base de datos real.
     * Es la MISMA ESTRUCTURA usada por el profesor para ViewModels dependientes de datos.
     */
    class FakeDao : AppUserDao {
        override suspend fun getUserByCorreo(correo: String) = null
        override suspend fun insertUser(user: AppUser) {}
        override suspend fun login(correo: String, clave: String) = null
    }

    private val usuarioVM = UsuarioViewModel()
    private val authVM = AuthViewModel(FakeDao())

    @Test
    fun testRegistroElementosEstaticos() {

        // Renderizamos la pantalla de registro dentro del entorno de prueba.
        // createComposeRule() crea una especie de "mini Activity" donde se puede
        // dibujar la interfaz con Jetpack Compose SOLO para este test.
        //
        // Dentro de setContent():
        // 1) navController = rememberNavController()
        //    - Creamos un NavController básico.
        //    - No hace navegación real (porque en los tests NO existe un NavHost).
        //    - Solo evita errores si la pantalla usa navController.navigate(...)

        // 2) Suministramos los ViewModels que la pantalla requiere:
        //    - usuarioVM  -> maneja los datos del usuario (nombre, correo, dirección…)
        //    - authVM     -> maneja autenticación (login/registro)
        //    Ambos se inyectan aquí igual que si fuese una Activity real.

        // 3) Finalmente, se renderiza RegistroScreen().
        //    Esto deja toda la UI lista para que el test pueda:
        //    - encontrar textos
        //    - escribir en los campos
        //    - verificar botones
        //    - comprobar estados
        composeTestRule.setContent {
            RegistroScreen(
                navController = rememberNavController(), // NavController falso
                viewModel = usuarioVM, // ViewModel del formulario
                authViewModel = authVM // ViewModel de autenticación
            )
        }

        // NAV superior
        composeTestRule.onNodeWithText("°-._ NoLimits _.-°")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Nombre").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apellidos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Teléfono (9 dígitos)").assertIsDisplayed()

    }

    @Test
    fun testRegistroElementosEstaticos2() {

        // Renderizamos la pantalla de registro dentro del entorno de prueba.
        // createComposeRule() crea una especie de "mini Activity" donde se puede
        // dibujar la interfaz con Jetpack Compose SOLO para este test.
        //
        // Dentro de setContent():
        // 1) navController = rememberNavController()
        //    - Creamos un NavController básico.
        //    - No hace navegación real (porque en los tests NO existe un NavHost).
        //    - Solo evita errores si la pantalla usa navController.navigate(...)

        // 2) Suministramos los ViewModels que la pantalla requiere:
        //    - usuarioVM  -> maneja los datos del usuario (nombre, correo, dirección…)
        //    - authVM     -> maneja autenticación (login/registro)
        //    Ambos se inyectan aquí igual que si fuese una Activity real.

        // 3) Finalmente, se renderiza RegistroScreen().
        //    Esto deja toda la UI lista para que el test pueda:
        //    - encontrar textos
        //    - escribir en los campos
        //    - verificar botones
        //    - comprobar estados

        composeTestRule.setContent {
            RegistroScreen(
                navController = rememberNavController(), // NavController falso
                viewModel = usuarioVM, // ViewModel del formulario
                authViewModel = authVM // ViewModel de autenticación
            )
        }
        composeTestRule.onNodeWithText("Dirección").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña (mín. 8 caracteres)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Repetir contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Acepto los términos y condiciones").assertExists()

        // IMPORTANTE: "Registrarse" e "Iniciar sesión" están al final del Scroll.
        // No se usan assertIsDisplayed() porque no están visibles en pantalla,
        // pero sí existen en el árbol semántico. Usamos assertExists().
        composeTestRule.onNodeWithText("Iniciar sesión").assertExists()
        composeTestRule.onNodeWithText("Registrarse").assertExists()

        // FOOTER
        composeTestRule
            .onNodeWithText("_.-°-._ All in One _.-°-._", ignoreCase = false)
            .assertIsDisplayed()

    }

    // -- Test 2 --
    @Test
    fun testRegistroInputs() {

        // Renderizamos la pantalla de registro dentro del entorno de prueba.
        // createComposeRule() crea una especie de "mini Activity" donde se puede
        // dibujar la interfaz con Jetpack Compose SOLO para este test.
        //
        // Dentro de setContent():
        // 1) navController = rememberNavController()
        //    - Creamos un NavController básico.
        //    - No hace navegación real (porque en los tests NO existe un NavHost).
        //    - Solo evita errores si la pantalla usa navController.navigate(...)

        // 2) Suministramos los ViewModels que la pantalla requiere:
        //    - usuarioVM  -> maneja los datos del usuario (nombre, correo, dirección…)
        //    - authVM     -> maneja autenticación (login/registro)
        //    Ambos se inyectan aquí igual que si fuese una Activity real.

        // 3) Finalmente, se renderiza RegistroScreen().
        //    Esto deja toda la UI lista para que el test pueda:
        //    - encontrar textos
        //    - escribir en los campos
        //    - verificar botones
        //    - comprobar estados

        composeTestRule.setContent {
            RegistroScreen(
                navController = rememberNavController(), // NavController falso
                viewModel = usuarioVM, // ViewModel del formulario
                authViewModel = authVM // ViewModel de autenticación
            )
        }

        // Usamos [0] porque OutlinedTextField crea 2 nodos con el mismo texto:
        // 1) Label -> "Nombre"
        // 2) Campo editable -> donde se ingresa el texto
        composeTestRule.onAllNodesWithText("Nombre")[0]
            .performTextInput("Martin")

        composeTestRule.onAllNodesWithText("Apellidos")[0]
            .performTextInput("Garrix")

        composeTestRule.onAllNodesWithText("Correo electrónico")[0]
            .performTextInput("martingarrix@gmail.com")

        // Verificar contenido ingresado
        composeTestRule.onNodeWithText("Martin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Garrix").assertIsDisplayed()
        composeTestRule.onNodeWithText("martingarrix@gmail.com").assertIsDisplayed()
    }
}