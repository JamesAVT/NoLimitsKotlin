package com.example.nolimits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.nolimits.navigation.NavigationEvent
import com.example.nolimits.ui.theme.NoLimitsTheme
import com.example.nolimits.ui.viewmodels.CartViewModel
import com.example.nolimits.ui.viewmodels.MainViewModel
import com.example.nolimits.ui.viewmodels.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest
import android.Manifest
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.nolimits.navigation.AppNavigation

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    // Launcher para pedir múltiples permisos (ubicación + galería)
    private lateinit var multiplePermissionsLauncher: ActivityResultLauncher<Array<String>>

    // Esto para saber si ya se pidieron permisos antes
    private var permisosYaPedidos = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cargar desde SharedPreferences si los permisos ya fueron pedidos previamene
        val sharedPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        permisosYaPedidos = sharedPrefs.getBoolean("permisos_ya_pedidos", false)

        // Registrar launcher que pedirá permisos
        multiplePermissionsLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->

                // Revisa si el usuario aceptó la ubicación
                val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

                // Revisa si aceptó la galería
                val galleryGranted =
                    permissions[Manifest.permission.READ_MEDIA_IMAGES] == true ||
                            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true

                // Si ambos permisos fueron aceptados
                if (locationGranted && galleryGranted) {
                    Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show()
                    sharedPrefs.edit().putBoolean("permisos_ya_pedidos", true).apply()
                } else {
                    // Si el usuario rechazó uno o ambos permisos
                    Toast.makeText(this, "Debes aceptar los permisos para continuar", Toast.LENGTH_SHORT).show()
                }
            }

        setContent {
            NoLimitsTheme {

                // Lanzar permisos una sola vez, al abrir la app
                LaunchedEffect(Unit) {
                    if (!permisosYaPedidos) {
                        multiplePermissionsLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        )
                    }
                }

                // ---------------- NAV & VIEWMODELS ----------------
                val mainViewModel: MainViewModel = viewModel()
                val usuarioViewModel: UsuarioViewModel = viewModel()
                val cartViewModel: CartViewModel = viewModel()
                val navController = rememberNavController()

                // Listener global que escucha eventos del MainViewModel para navegar entre pantallas
                LaunchedEffect(Unit) {
                    mainViewModel.navigationEvents.collectLatest { event ->
                        when (event) {

                            // Navega a una ruta específica
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route) {
                                    event.popUpToRoute?.let {
                                        popUpTo(it.route) { inclusive = event.inclusive }
                                    }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                            }
                            // Retroceder a una pantalla
                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                            // Rectroceder usando "navigateUp"
                            is NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }

                // Contenedor principal de la pantalla (topBarm bottomBar, contenido)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Sistema completo de navegación entre pantallas
                    AppNavigation(
                        navController = navController,
                        viewModel = usuarioViewModel,
                        mainViewModel = mainViewModel,
                        cartViewModel = cartViewModel,
                        startAtPrincipal = true
                    )
                }
            }
        }
    }
}
