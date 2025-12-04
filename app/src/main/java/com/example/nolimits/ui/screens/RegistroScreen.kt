package com.example.nolimits.ui.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nolimits.data.datastore.EstadoDataStore
import com.example.nolimits.ui.viewmodels.UsuarioViewModel
import com.example.nolimits.ui.viewmodels.AuthViewModel

/*
    Pantalla de registro de usuario.
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel,
    authViewModel: AuthViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val context = LocalContext.current

    // Campos locales
    var apellidos by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var repetirClave by rememberSaveable { mutableStateOf("") }

    var showPass by rememberSaveable { mutableStateOf(false) }
    var showPass2 by rememberSaveable { mutableStateOf(false) }

    val dataStore = EstadoDataStore(context)

    val scroll = rememberScrollState()

    // 1. --- VALIDACIONES INDIVIDUALES ---
    // Chequeos de formato y existencia:

    val nombreValido = estado.nombre.isNotBlank() && estado.nombre.all { it.isLetter() || it.isWhitespace() }
    val apellidosValidos = apellidos.isNotBlank() && apellidos.all { it.isLetter() || it.isWhitespace() }

    // CORRECCIÓN APLICADA: Usar estado.correo.isNotBlank()
    val correoValido = estado.correo.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(estado.correo).matches()

    val telefonoValido = telefono.isNotBlank() && telefono.length == 9 && telefono.all { it.isDigit() }
    val direccionValida = estado.direccion.isNotBlank()
    val claveValida = estado.clave.isNotBlank() && estado.clave.length >= 8
    val repetirClaveValida = repetirClave.isNotBlank() && estado.clave == repetirClave

    // 2. --- VALIDACIÓN GLOBAL (Determina si el botón se habilita) ---
    val formularioValido = nombreValido && apellidosValidos && correoValido && telefonoValido &&
            direccionValida && claveValida && repetirClaveValida && estado.aceptaTerminos

    // ----------------------------------------------------------------


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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
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
                    // La propiedad isBlank está disponible en String, no en el objeto 'estado' completo.
                }
            }
        }
    ) { inner ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = inner.calculateTopPadding() + 8.dp,
                    bottom = inner.calculateBottomPadding() + 8.dp,
                    start = 24.dp,
                    end = 24.dp
                )
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Nombre
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = {
                    if (it.all { c -> c.isLetter() || c.isWhitespace() })
                        viewModel.onNombreChange(it)
                },
                label = { Text("Nombre") },
                isError = estado.nombre.isNotBlank() && !nombreValido,
                supportingText = {
                    if (estado.nombre.isNotBlank() && !nombreValido) {
                        Text("El nombre solo debe contener letras.", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            // Apellidos
            OutlinedTextField(
                value = apellidos,
                onValueChange = {
                    if (it.all { c -> c.isLetter() || c.isWhitespace() })
                        apellidos = it
                },
                label = { Text("Apellidos") },
                isError = apellidos.isNotBlank() && !apellidosValidos,
                supportingText = {
                    if (apellidos.isNotBlank() && !apellidosValidos) {
                        Text("Apellidos inválidos.", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            // Correo
            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo electrónico") },
                isError = estado.correo.isNotBlank() && !correoValido,
                supportingText = {
                    if (estado.correo.isNotBlank() && !correoValido) {
                        Text("Correo electrónico inválido.", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            // Teléfono
            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    if (it.all { c -> c.isDigit() })
                        telefono = it.take(9)
                },
                label = { Text("Teléfono (9 dígitos)") },
                isError = telefono.isNotBlank() && !telefonoValido,
                supportingText = {
                    if (telefono.isNotBlank() && !telefonoValido) {
                        Text("Ej: 912345678.", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            // Dirección
            OutlinedTextField(
                value = estado.direccion,
                onValueChange = viewModel::onDireccionChange,
                label = { Text("Dirección") },
                isError = estado.direccion.isNotBlank() && !direccionValida,
                supportingText = {
                    // Si el campo tiene contenido, pero la única regla es que no esté vacío,
                    // no mostramos mensaje aquí ya que la validación global manejará el campo vacío.
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            // Contraseña
            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::onClaveChange,
                label = { Text("Contraseña (mín. 8 caracteres)") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (showPass) "Ocultar" else "Mostrar"
                        )
                    }
                },
                isError = estado.clave.isNotBlank() && !claveValida,
                supportingText = {
                    if (estado.clave.isNotBlank() && !claveValida) {
                        Text("Debe tener al menos 8 caracteres.", color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

            // Repetir contraseña
            OutlinedTextField(
                value = repetirClave,
                onValueChange = { repetirClave = it },
                label = { Text("Repetir contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPass2) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPass2 = !showPass2 }) {
                        Icon(
                            if (showPass2) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (showPass2) "Ocultar" else "Mostrar"
                        )
                    }
                },
                isError = repetirClave.isNotBlank() && !repetirClaveValida,
                supportingText = {
                    if (repetirClave.isNotBlank() && !repetirClaveValida) {
                        Text("Las contraseñas no coinciden.", color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            // Aceptar términos
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = viewModel::onAceptarTerminosChange
                )

                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Acepto los términos y condiciones",
                    modifier = Modifier.clickable {
                        viewModel.onAceptarTerminosChange(!estado.aceptaTerminos)
                    }
                )
            }
            if (!formularioValido && !estado.aceptaTerminos) {
                Text(
                    "Debes aceptar los términos y condiciones.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp) // Añadir padding para alineación
                )
            }

            // Botón registrar (Room)
            Button(
                onClick = {

                    authViewModel.registrarUsuario(
                        nombre = estado.nombre,
                        apellidos = apellidos,
                        correo = estado.correo,
                        telefono = telefono,
                        direccion = estado.direccion,
                        clave = estado.clave,
                        dataStore = dataStore,   // ← LLAVE PARA GUARDAR userId
                        onSuccess = {
                            Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
                            navController.navigate("signin") {
                                popUpTo("registro") { inclusive = true }
                            }
                        },
                        onError = { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    )

                },
                enabled = formularioValido,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            // Mensaje de error general
            if (!formularioValido) {
                Text(
                    "Completa todos los campos correctamente para continuar.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Enlace para ir a iniciar sesión
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(text = "¿Ya tienes una cuenta?")
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Iniciar sesión",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate("signin")
                    }
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}