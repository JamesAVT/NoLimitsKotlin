// Ruta: app/src/main/java/com/example/nolimits/navigation/AppNavigation.kt
package com.example.nolimits.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nolimits.data.local.model.database.AppDatabase
import com.example.nolimits.domain.Product
import com.example.nolimits.ui.screens.BoletaComprobante
import com.example.nolimits.ui.screens.CatalogScreen
import com.example.nolimits.ui.screens.CartScreen
import com.example.nolimits.ui.screens.PantallaMetodoPago
import com.example.nolimits.ui.screens.PantallaPrincipal
import com.example.nolimits.ui.screens.RecuperarContrasenaScreen
import com.example.nolimits.ui.screens.RegistroScreen
import com.example.nolimits.ui.screens.ResumenScreen
import com.example.nolimits.ui.screens.SignInScreen
import com.example.nolimits.ui.viewmodels.AuthViewModel
import com.example.nolimits.ui.viewmodels.AuthViewModelFactory
import com.example.nolimits.ui.viewmodels.CartViewModel
import com.example.nolimits.ui.viewmodels.MainViewModel
import com.example.nolimits.ui.viewmodels.UsuarioViewModel

/**
 * Contiene el NavHost principal de la app.
 * Registra todas las pantallas y define las rutas de navegación.
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: UsuarioViewModel,
    mainViewModel: MainViewModel,
    cartViewModel: CartViewModel,
    startAtPrincipal: Boolean = false
) {
    // ─────────── ROOM + AUTHVIEWMODEL ───────────
    val context = LocalContext.current

    // Base de datos local (Room)
    val db = AppDatabase.getDatabase(context)

    // DAO de usuario para autenticación
    val appUserDao = db.appUserDao()

    // ViewModel de autenticación (login / registro)
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(appUserDao)
    )

    // ─────────── NAVHOST ───────────
    NavHost(
        navController = navController,
        startDestination = if (startAtPrincipal) {
            Screen.PantallaPrincipal.route
        } else {
            Screen.SignIn.route
        }
    ) {

        // PANTALLA PRINCIPAL
        composable(Screen.PantallaPrincipal.route) {
            PantallaPrincipal(navController = navController)
        }

        // AUTENTICACIÓN: LOGIN
        composable(Screen.SignIn.route) {
            SignInScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // AUTENTICACIÓN: REGISTRO
        composable(Screen.Register.route) {
            RegistroScreen(
                navController = navController,
                viewModel = viewModel,
                authViewModel = authViewModel
            )
        }

        // RECUPERAR CONTRASEÑA
        composable(Screen.Recover.route) {
            RecuperarContrasenaScreen(navController = navController)
        }

        // CATÁLOGO
        composable(Screen.Catalog.route) {
            CatalogScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        // CARRITO
        composable(Screen.Cart.route) {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        // MÉTODO DE PAGO
        composable(Screen.MetodoPago.route) {
            PantallaMetodoPago(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        // BOLETA DE COMPRA
        composable("boleta") { backStackEntry ->
            val items = backStackEntry.savedStateHandle
                .get<List<Product>>("itemsComprados") ?: emptyList()
            val total = backStackEntry.savedStateHandle
                .get<Double>("total") ?: 0.0
            val ultimos4 = backStackEntry.savedStateHandle
                .get<String>("ultimos4")

            BoletaComprobante(
                navController = navController,
                itemsComprados = items,
                total = total,
                ultimos4 = ultimos4
            )
        }

        // RESUMEN DEL REGISTRO
        composable(Screen.Summary.route) {
            ResumenScreen(viewModel = viewModel)
        }
    }
}