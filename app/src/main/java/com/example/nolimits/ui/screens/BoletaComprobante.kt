package com.example.nolimits.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nolimits.domain.Product
import com.example.nolimits.navigation.Screen
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

/*
    Comprobante generado después de la compra.
    Muestra fecha, total, productos, últimos dígitos, etc.
*/

// Es una anotación para decirle a Compose: “Quiero usar funciones que todavía son EXPERIMENTALES. Entiendo los riesgos.”
@OptIn(ExperimentalMaterial3Api::class)
// @Composable es una anotación que convierte una función de Kotlin en una función que puede dibujar UI usando Jetpack Compose.
@Composable
fun BoletaComprobante(
    navController: NavController,
    itemsComprados: List<Product>,
    total: Double,
    ultimos4: String? = null
) {
    val clp = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }
    val fecha = remember { SimpleDateFormat("dd/MM/yyyy", Locale("es", "CL")).format(Date()) }
    val hora = remember { SimpleDateFormat("HH:mm", Locale("es", "CL")).format(Date()) }
    val idCompra = remember { "NL-${Random.nextInt(100000, 999999)}" }

    val nombre = navController.previousBackStackEntry?.savedStateHandle?.get<String>("nombre") ?: "No registrado"
    val apellidos = navController.previousBackStackEntry?.savedStateHandle?.get<String>("apellidos") ?: ""
    val direccion = navController.previousBackStackEntry?.savedStateHandle?.get<String>("direccion") ?: "No registrado"
    val region = navController.previousBackStackEntry?.savedStateHandle?.get<String>("region") ?: "No registrado"

    // Es un contenedor que te da la estructura básica de una pantalla.
    Scaffold(
        // NAV
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "°-._ NoLimits _.-°",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },

        // FOOTER negro centrado
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
    ) { inner ->
        // Contenido centrado
        Box(
            // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentAlignment = Alignment.Center
        ) {
            Card(
                // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Encabezado de la boleta
                    Text("🧾 Comprobante de Compra", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "Simulación de compra",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    // Spacer es un composable que sirve para crear un espacio vacío, ya sea vertical o horizontal.
                    Spacer(Modifier.height(6.dp))
                    Text("ID de compra: $idCompra", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Text("Fecha: $fecha    Hora: $hora", style = MaterialTheme.typography.bodyMedium)
                    // Divider muestra una línea horizontal (o vertical) para separar contenido.
                    Divider(Modifier.padding(vertical = 12.dp))

                    // Datos del comprador
                    Column(Modifier.fillMaxWidth()) {
                        Text("Cliente: ${listOf(nombre, apellidos).filter { it.isNotBlank() }.joinToString(" ")}",
                            style = MaterialTheme.typography.bodyMedium)
                        Text("Dirección: $direccion", style = MaterialTheme.typography.bodyMedium)
                        Text("Región: $region", style = MaterialTheme.typography.bodyMedium)
                    }
                    // Divider muestra una línea horizontal (o vertical) para separar contenido.
                    Divider(Modifier.padding(vertical = 12.dp))

                    // Detalle de ítems
                    LazyColumn(
                        // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 220.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(itemsComprados) { product ->
                            Row(
                                // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(product.name, style = MaterialTheme.typography.bodyMedium)
                                Text(clp.format(product.price), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    // Divider muestra una línea horizontal (o vertical) para separar contenido.
                    Divider(Modifier.padding(vertical = 12.dp))

                    // Método de pago y total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Método de pago: ${ultimos4?.let { "Tarjeta **** $it" } ?: "Simulado"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text("Total: ${clp.format(total)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }

                    // Spacer es un composable que sirve para crear un espacio vacío, ya sea vertical o horizontal.
                    Spacer(Modifier.height(20.dp))

                    // Botón principal → redirige al catálogo
                    Button(
                        onClick = {
                            navController.navigate(Screen.Catalog.route) {
                                // Elimina pantallas del backstack.
                                popUpTo(Screen.Catalog.route) { inclusive = true }
                                // Evita duplicar la pantalla.
                                launchSingleTop = true
                            }
                        },
                        // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver a NoLimits")
                    }
                }
            }
        }
    }
}