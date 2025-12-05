// Ruta: app/src/main/java/com/example/nolimits/ui/screens/RecuperarContrasenaScreen.kt
package com.example.nolimits.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nolimits.data.local.model.database.AppDatabase
import com.example.nolimits.ui.viewmodels.AuthViewModel
import com.example.nolimits.ui.viewmodels.AuthViewModelFactory

/*
    Pantalla para recuperar contraseña por correo.
*/

/**
 * Overload “simple” para usar en tests o en navegación básica.
 * Crea el AuthViewModel internamente con su factory.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarContrasenaScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val appUserDao = AppDatabase.getInstance(context).appUserDao()

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(appUserDao)
    )

    RecuperarContrasenaScreen(
        navController = navController,
        authViewModel = authViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarContrasenaScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var correo by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "°-._ NoLimits _.-°",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("signin") {
                                popUpTo("recuperar_contrasenia") { inclusive = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "_.-°-._ All in One _.-°-._",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Recuperar Contraseña",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ingresa tu correo electrónico y validaremos si está registrado. " +
                        "Si corresponde, se enviarán instrucciones para crear una nueva contraseña.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Ingresa tu correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        correo.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Por favor, ingresa tu correo electrónico.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        !correo.contains("@") || !correo.contains(".") -> {
                            Toast.makeText(
                                context,
                                "Por favor, ingresa un correo válido.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            isLoading = true

                            authViewModel.recuperarContrasena(
                                correo = correo,
                                onSuccess = {
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Instrucciones enviadas a tu correo.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate("signin") {
                                        popUpTo("recuperar_contrasenia") { inclusive = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onError = { mensaje ->
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        mensaje,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(if (isLoading) "Validando..." else "Continuar")
            }
        }
    }
}