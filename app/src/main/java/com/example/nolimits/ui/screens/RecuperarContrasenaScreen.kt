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

/*
    Pantalla para recuperar contraseña por correo.
*/

// Es una anotación para decirle a Compose: “Quiero usar funciones que todavía son EXPERIMENTALES. Entiendo los riesgos.”
@OptIn(ExperimentalMaterial3Api::class)
// @Composable es una anotación que convierte una función de Kotlin en una función que puede dibujar UI usando Jetpack Compose.
@Composable
fun RecuperarContrasenaScreen(navController: NavController) {

    var correo by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Es un contenedor que te da la estructura básica de una pantalla.
    Scaffold(
        // NAV superior negro con flecha blanca
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
                        // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
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

        // FOOTER negro con texto centrado
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                Box(
                    // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
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
            // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
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

            // Spacer es un composable que sirve para crear un espacio vacío, ya sea vertical o horizontal.
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ingresa tu correo electrónico y te enviaremos las instrucciones para crear una nueva contraseña. Una vez hecho, se te dirigirá a la página principal.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Spacer es un composable que sirve para crear un espacio vacío, ya sea vertical o horizontal.
            // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Ingresa tu correo") },
                // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                modifier = Modifier.fillMaxWidth()
            )

            // Spacer es un composable que sirve para crear un espacio vacío, ya sea vertical o horizontal.
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
                        }
                    }
                },
                // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Continuar")
            }
        }
    }
}