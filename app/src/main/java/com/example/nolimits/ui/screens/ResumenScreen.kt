package com.example.nolimits.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nolimits.ui.viewmodels.UsuarioViewModel

/*
    Muestra el resumen final del proceso (tipo "Gracias por comprar").
*/

// @Composable es una anotación que convierte una función de Kotlin en una función que puede dibujar UI usando Jetpack Compose.
@Composable
fun ResumenScreen(viewModel: UsuarioViewModel) {
    val estado by viewModel.estado.collectAsState()

           // Modifier es un objeto que le dice al Composable: cómo se debe ver, cómo debe comportarse y cómo debe posicionarse.
    Column(Modifier.padding(all = 16.dp)) {
        Text(
            text = "Resumen del Registro",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "Nombre: ${estado.nombre}")
        Text(text = "Correo: ${estado.correo}")
        Text(text = "Dirección: ${estado.direccion}")
        Text(text = "Contraseña: ${"*".repeat(estado.clave.length)}")
        Text(
            text = "Términos: ${
                if (estado.aceptaTerminos) "Aceptados" else "No aceptados"
            }"
        )
    }
}