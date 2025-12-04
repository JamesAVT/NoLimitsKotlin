package com.example.nolimits.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nolimits.domain.Product
import com.example.nolimits.navigation.Screen
import com.example.nolimits.ui.viewmodels.CartViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

/*
    Pantalla del carrito.
    Muestra items agregados.
    Llama a CartViewModel.
*/

// Es una anotación para decirle a Compose: “Quiero usar funciones que todavía son EXPERIMENTALES. Entiendo los riesgos.”
@OptIn(ExperimentalMaterial3Api::class)
// @Composable es una anotación que convierte una función de Kotlin en una función que puede dibujar UI usando Jetpack Compose.
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    val cartItems = cartViewModel.cartItems
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Es un contenedor que te da la estructura básica de una pantalla.
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

        // NAV superior (negro) con título y flecha de volver BLANCA.
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "°-._ NoLimits _.-°",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },

        // FOOTER negro.
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
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("🛒 Carrito de compras", style = MaterialTheme.typography.headlineMedium)
            // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
            Spacer(Modifier.height(12.dp))

            if (cartItems.isEmpty()) {
                Text("Tu carrito está vacío!")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItems) { product ->
                        Row(
                            // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${product.name} - $${product.price}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = {
                                    cartViewModel.removeFromCart(product)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("❌ Producto eliminado!")
                                    }
                                }
                            ) { Text("Eliminar") }
                        }
                    }
                }

                // Spacer es un composable que sirve para crear un espacio vacío, ya sea vertical o horizontal.
                Spacer(Modifier.height(16.dp))
                Text(
                    "Total: ${
                        NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                            .format(cartViewModel.getTotal())
                    }",
                    style = MaterialTheme.typography.titleMedium
                )

                Button(
                    onClick = {
                        val itemsComprados: List<Product> = cartViewModel.cartItems.toList()
                        val total = cartViewModel.getTotal()

                        // Dejamos datos en el BackStack (los leerá PantallaMetodoPago)
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("itemsComprados", itemsComprados)
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("total", total)

                        // Vamos a Método de Pago (el carrito se limpia tras confirmar pago)
                        navController.navigate(Screen.MetodoPago.route) {
                            launchSingleTop = true
                        }
                    },
                    // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) { Text("Ir a método de pago") }
            }
        }
    }
}