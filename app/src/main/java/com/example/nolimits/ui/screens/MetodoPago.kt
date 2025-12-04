package com.example.nolimits.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nolimits.data.datastore.EstadoDataStore
import com.example.nolimits.domain.Product
import com.example.nolimits.navigation.Screen
import com.example.nolimits.ui.viewmodels.CartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/*
    Composable para mostrar formas de pago.
*/

// Es una anotación para decirle a Compose: “Quiero usar funciones que todavía son EXPERIMENTALES. Entiendo los riesgos.”
@OptIn(ExperimentalMaterial3Api::class)
// @Composable es una anotación que convierte una función de Kotlin en una función que puede dibujar UI usando Jetpack Compose.
@Composable
fun PantallaMetodoPago(
    navController: NavController,  // Para navegar entre pantallas
    cartViewModel: CartViewModel   // Para obtener total y vaciar el carrito
) {
    // Control para cerrar el teclado cuando se aprieta "Done"
    val foco = LocalFocusManager.current

    // Variables de estado

    var nombreTitular by rememberSaveable { mutableStateOf("") }
    var numeroTarjeta by rememberSaveable (stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var fechaVencimiento by rememberSaveable (stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("")) }
    var codigoCvv by rememberSaveable { mutableStateOf("") }
    var guardarTarjeta by rememberSaveable { mutableStateOf(true) }

    // Toma el total desde la pantalla anterior. Si no existe, usa el ViewModel
    val total = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Double>("total") ?: cartViewModel.getTotal()

    // Validaciones del formulario

    val nombreValido = nombreTitular.trim().contains(" ") && nombreTitular.trim().length >= 6
    val digitosTarjeta = numeroTarjeta.text.filter { it.isDigit() }
    val numeroValido = digitosTarjeta.length == 16
    val vencimientoValido = run {
        val p = fechaVencimiento.text.split("/")
        if (p.size == 2) {
            val mm = p[0].toIntOrNull()
            val aa = p[1].toIntOrNull()
            (mm != null && mm in 1..12 && aa != null && aa in 0..99)
        } else false
    }
    val cvvValido = codigoCvv.length in 3..4 && codigoCvv.all { it.isDigit() }
    // El formulario solo se habilita si todo es válido
    val formularioValido = nombreValido && numeroValido && vencimientoValido && cvvValido

    // Función para formatear tarjeta
    fun formatearTarjeta(s: String) = s.filter { it.isDigit() }.chunked(4).joinToString(" ")

    // Función para formatear vencimiento
    fun formatearVenc(s: String): String {
        val d = s.filter { it.isDigit() }.take(4)
        return buildString {
            for (i in d.indices) {
                append(d[i])
                if (i == 1 && d.length > 2) append("/")
            }
        }
    }

    // Contexto actual de la app
    val context = LocalContext.current

    // Instancia del DataStore
    val dataStore = EstadoDataStore(context)


    // Estructura de pantalla

    // Es un contenedor que te da la estructura básica de una pantalla.
    Scaffold(
        // Barra superior
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
                    // Botón de volver
                    IconButton(
                        onClick = {
                            // Navega al carrito y borra pantalla actual del stack
                            navController.navigate(Screen.Cart.route) {
                                popUpTo(Screen.MetodoPago.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver al carrito",
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
        // Barra inferior
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
    ) { pad ->

        // Contenido principal
        Box(
            // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
            modifier = Modifier
                .fillMaxSize()
                .padding(pad),
            contentAlignment = Alignment.Center
        ) {
            Column(
                // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Total a pagar
                Text("Total: $${"%,.0f".format(total)}", style = MaterialTheme.typography.titleMedium)

                // CAMPO nombre del titular
                OutlinedTextField(
                    value = nombreTitular,
                    onValueChange = { nombreTitular = it },
                    label = { Text("Nombre del titular") },
                    singleLine = true,
                    isError = nombreTitular.isNotBlank() && !nombreValido,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                    modifier = Modifier.fillMaxWidth()
                )

                // CAMPO npumero de tarjeta con formateo automático

                OutlinedTextField(
                    value = numeroTarjeta,
                    onValueChange = { newValue ->
                        // Obtiene solo dígitos y limita a 16
                        val digits = newValue.text.filter { it.isDigit() }.take(16)

                        // Formatea en bloques de 4
                        val formatted = digits.chunked(4).joinToString(" ")

                        // Calcula cuántos dígitos había antes del cursor en lo que escribío el usuario
                        val digitsBeforeCursor = newValue.text
                            .take(newValue.selection.start)
                            .count { it.isDigit() }

                        // Calculamos posición correcta del cursor en el texto
                        val newCursorPos =
                            (digitsBeforeCursor + digitsBeforeCursor / 4).coerceAtMost(formatted.length)

                        // Actualizamos el estado completo (texto + cursor)
                        numeroTarjeta = TextFieldValue(
                            text = formatted,
                            selection = TextRange(newCursorPos)
                        )
                    },
                    label = { Text("Número de tarjeta (16 dígitos)") },
                    singleLine = true,
                    isError = numeroTarjeta.text.filter { it.isDigit() }.length != 16 &&
                            numeroTarjeta.text.isNotBlank(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // CAMPO fecha vencimiento con formateo
                    OutlinedTextField(
                        value = fechaVencimiento,
                        onValueChange = { newValue ->

                            // Solo dígitos y máximo 4
                            val digits = newValue.text.filter { it.isDigit() }.take(4)

                            // Formateo MM/AA
                            val formatted = buildString {
                                digits.forEachIndexed { index, c ->
                                    append(c)
                                    if (index == 1 && digits.length > 2) append("/")
                                }
                            }

                            // Cuántos dígitos había antes del cursor original
                            val digitsBeforeCursor = newValue.text
                                .take(newValue.selection.start)
                                .count { it.isDigit() }

                            // Calculamos nueva posición del cursor en el formateado
                            val cursorPos = when {
                                digitsBeforeCursor <= 1 -> digitsBeforeCursor
                                digitsBeforeCursor == 2 -> 3 // salta el "/"
                                digitsBeforeCursor > 2 -> (digitsBeforeCursor + 1)
                                else -> formatted.length
                            }.coerceAtMost(formatted.length)

                            // Actualizamos el estado
                            fechaVencimiento = TextFieldValue(
                                text = formatted,
                                selection = TextRange(cursorPos)
                            )
                        },
                        label = { Text("Vencimiento (MM/AA)") },
                        singleLine = true,
                        isError = fechaVencimiento.text.length == 5 && (
                                fechaVencimiento.text.take(2).toIntOrNull() !in 1..12
                                ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                        modifier = Modifier.weight(1f)
                    )

                    // CAMPO CVV
                    OutlinedTextField(
                        value = codigoCvv,
                        onValueChange = { codigoCvv = it.filter { c -> c.isDigit() }.take(4) },
                        label = { Text("CVV") },
                        singleLine = true,
                        isError = codigoCvv.isNotBlank() && !cvvValido,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { foco.clearFocus() }),
                        // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
                        modifier = Modifier.weight(1f)
                    )
                }

                // Checkbox guardar tarjeta
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = guardarTarjeta, onCheckedChange = { guardarTarjeta = it })
                    Text("Guardar tarjeta para próximas compras (simulación)")
                }

                Button(
                    onClick = {
                        val items = navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.get<List<Product>>("itemsComprados")
                            ?: emptyList()

                        // Registrar venta REAL en Swagger usando corrutina
                        CoroutineScope(Dispatchers.IO).launch {

                            // PASO IMPORTANTE:
                            // Ahora registrarVentaEnSwagger recibe el dataStore
                            val ok = cartViewModel.registrarVentaEnSwagger(dataStore)

                            withContext(Dispatchers.Main) {
                                if (ok) {
                                    // Venta registrada OK → continuar como antes
                                    navController.navigate("boleta") {
                                        launchSingleTop = true
                                    }

                                    navController.currentBackStackEntry?.savedStateHandle?.set("itemsComprados", items)
                                    navController.currentBackStackEntry?.savedStateHandle?.set("total", total)
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "ultimos4",
                                        digitosTarjeta.takeLast(4)
                                    )

                                    cartViewModel.clearCart()

                                } else {
                                    // Error si no se registró
                                    Toast.makeText(
                                        navController.context,
                                        "Error al registrar la venta en el servidor",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    },
                    enabled = formularioValido,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirmar y pagar")
                }

                // Mensaje de error por si falta algo
                if (!formularioValido) {
                    Text(
                        "Completa todos los campos correctamente para continuar.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}