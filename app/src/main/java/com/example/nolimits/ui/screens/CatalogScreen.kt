// Ruta: app/src/main/java/com/example/nolimits/ui/screens/CatalogScreen.kt
package com.example.nolimits.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nolimits.R
import com.example.nolimits.domain.Product
import com.example.nolimits.navigation.Screen
import com.example.nolimits.ui.viewmodels.CartViewModel
import com.example.nolimits.ui.viewmodels.CatalogViewModel
import kotlinx.coroutines.launch
import java.text.Normalizer
import java.text.NumberFormat
import java.util.Locale

/**
 * Pantalla del catálogo.
 *
 * - Mantiene la estructura visual original (secciones, cards, imágenes, descripción).
 * - Los productos se obtienen desde CatalogViewModel, que a su vez consume la API.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    viewModel: CatalogViewModel = viewModel()
) {
    // Datos expuestos por el ViewModel (API)
    val products by viewModel.products.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val money = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Agrupación de productos por categoría
    val groupedProducts: Map<String, List<Product>> = remember(products) {
        products.groupBy { categoryKey(it) }
    }
    val sectionOrder = listOf("Películas", "Videojuegos", "Accesorios")

    // Estructura principal de pantalla
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "°-._ NoLimits _.-°",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // Navega a la pantalla principal y limpia el back stack
                            navController.navigate("pantalla_principal") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "null",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                        Text(
                            "🛒",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
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
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    // Encabezado con texto y logotipo
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "_.- Variedad, estilo y calidad en un solo lugar -._\n",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                            Image(
                                painter = painterResource(id = R.drawable.nolimitslogo),
                                contentDescription = "Logo NoLimits",
                                modifier = Modifier
                                    .size(260.dp)
                                    .padding(bottom = 4.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    // Secciones del catálogo (películas, videojuegos, accesorios)
                    sectionOrder.forEach { section ->
                        val list = when (section) {
                            "Accesorios" ->
                                groupedProducts["Accesorios"].orEmpty() +
                                        groupedProducts["Otros"].orEmpty()

                            else -> groupedProducts[section].orEmpty()
                        }

                        if (list.isNotEmpty()) {
                            stickyHeader {
                                Surface(color = MaterialTheme.colorScheme.background) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = section,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Divider(thickness = 1.dp)
                                    }
                                }
                            }

                            items(list, key = { it.name }) { p ->
                                val expanded = rememberSaveable(p.name) {
                                    mutableStateOf(false)
                                }

                                ElevatedCard(Modifier.fillMaxWidth()) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        // Imagen asociada al producto (si existe)
                                        if (p.imageRes != null) {
                                            val isMovie = section == "Películas"
                                            Image(
                                                painter = painterResource(id = p.imageRes),
                                                contentDescription = p.name,
                                                modifier = Modifier
                                                    .fillMaxWidth(0.95f)
                                                    .height(
                                                        if (isMovie) 180.dp
                                                        else 145.dp
                                                    )
                                                    .clip(MaterialTheme.shapes.medium),
                                                contentScale = ContentScale.FillHeight
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Filled.PhotoCamera,
                                                contentDescription = p.name,
                                                tint = Color.Unspecified,
                                                modifier = Modifier.size(40.dp)
                                            )
                                        }

                                        // Nombre del producto
                                        Text(
                                            text = p.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )

                                        // Precio formateado en CLP
                                        Text(
                                            text = money.format(p.price),
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )

                                        // Botones de acciones: ver más y agregar al carrito
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            OutlinedButton(
                                                onClick = {
                                                    expanded.value = !expanded.value
                                                },
                                                modifier = Modifier.weight(1f),
                                                contentPadding = PaddingValues(
                                                    horizontal = 8.dp,
                                                    vertical = 6.dp
                                                )
                                            ) {
                                                Text(
                                                    if (expanded.value)
                                                        "Menos información"
                                                    else
                                                        "Más información"
                                                )
                                            }

                                            Button(
                                                onClick = {
                                                    cartViewModel.addToCart(p)
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            "Producto agregado"
                                                        )
                                                    }
                                                },
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Agregar al carrito")
                                            }
                                        }
                                    }

                                    if (expanded.value) {
                                        Divider()
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                        ) {
                                            Text(
                                                text = productDescription(p),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            // Indicador de carga
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Mensaje de error
            error?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}

/**
 * Calcula la categoría base para agrupar el producto en la lista del catálogo.
 */
private fun categoryKey(product: Product): String {
    return when (product.tipoProducto.lowercase()) {
        "película", "pelicula" -> "Películas"
        "videojuego" -> "Videojuegos"
        "accesorio" -> "Accesorios"
        else -> "Otros"
    }
}

/**
 * Normaliza un texto para facilitar comparaciones insensibles a acentos y símbolos.
 */
private fun normalizeKey(raw: String): String {
    val s0 = raw.lowercase()
        .replace('’', '\'')
        .replace('´', '\'')
        .replace('“', '"')
        .replace('”', '"')
        .replace(Regex("\\(\\d{4}\\)"), "")

    val s1 = Normalizer.normalize(s0, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()

    return s1
}

/**
 * Determina si el nombre normalizado corresponde a un accesorio.
 */
private fun isAccessory(n: String): Boolean =
    n.contains("dualsense") || n.contains("control") || n.contains("headset") ||
            n.contains("auricular") || n.contains("audifono") || n.contains("audifonos") ||
            n.contains("audífono") || n.contains("audífonos") || n.contains("figura") ||
            n.contains("mascara") || n.contains("máscara")

/**
 * Determina si el nombre normalizado corresponde a un videojuego.
 */
private fun isVideogame(n: String): Boolean =
    n.contains("miles morales") || n.contains("spider man remastered") ||
            (n.contains("spider man 2") && (n.contains("marvel s") || n.contains("marvels")))

/**
 * Genera una descripción contextual basada en el nombre del producto.
 */
private fun productDescription(p: Product): String {
    val n = normalizeKey(p.name)

    if (n.contains("dualsense") || (n.contains("control") && n.contains("spider man"))) {
        return """
            Título completo: DualSense Spider-Man.
            Autor: Sony Interactive Entertainment.
            Fecha: 2023.
            Imagen destacada: Edición especial con patrón de simbionte.
            Contenido del artículo: Diseño negro y rojo, logo de la araña, base de carga LED. Precisión, comodidad y hápticos.
            ¿Qué opinas de este accesorio? Compártenos tu experiencia en NoLimitsCorp@gmail.com.
        """.trimIndent()
    }

    if (n.contains("audifono") || n.contains("audifonos") || n.contains("audífono") ||
        n.contains("audífonos") || n.contains("headset") || n.contains("auricular")
    ) {
        return """
            Título completo: Audífonos Xtech Spiderman Headset WRD LED.
            Autor: Xtech.
            Fecha: 2022.
            Imagen destacada: Edición Spider-Man con iluminación LED.
            Contenido del artículo: Micrófono ajustable, buen confort y sonido envolvente para sesiones largas.
            ¿Qué opinas de este accesorcio? Compártenos tu experiencia en NoLimitsCorp@gmail.com.
        """.trimIndent()
    }

    if (n.contains("mascara") || n.contains("máscara")) {
        return """
            Título completo: Máscara Electrónica de Spider-Man.
            Autor: Marvel.
            Imagen destacada: Máscara con detalles electrónicos.
            Contenido del artículo: Adecuada para cosplay o juego temático.
            ¿Qué opinas de este accesorio? Compártenos tu experiencia en NoLimitsCorp@gmail.com.
        """.trimIndent()
    }

    if (n.contains("miles morales")) {
        return """
            Título completo: Spider-Man: Miles Morales.
            Autor: Insomniac Games y Marvel Games.
            Fecha: 2020.
            Imagen destacada: Miles balanceándose en Harlem.
            Contenido del artículo: Poderes bioeléctricos, camuflaje y mundo abierto ambientado en Harlem.
            ¿Qué opinas de este juego? Compártenos tu experiencia en NoLimitsCorp@gmail.com.
        """.trimIndent()
    }

    if (n.contains("spider man remastered")) {
        return """
            Título completo: Marvel's Spider-Man Remastered.
            Autor: Insomniac Games y Marvel Games.
            Fecha: 2020.
            Imagen destacada: Spider-Man sobre Nueva York.
            Contenido del artículo: Combate dinámico, uso de gadgets y recreación detallada de la ciudad.
            ¿Qué opinas de este juego? Compártenos tu experiencia en NoLimitsCorp@gmail.com.
        """.trimIndent()
    }

    if (n.contains("spider man 2") && (n.contains("marvel s") || n.contains("marvels"))) {
        return """
            Título completo: Marvel’s Spider-Man 2.
            Autor: Insomniac Games y Marvel Games.
            Fecha: 2023.
            Imagen destacada: Peter y Miles frente a Venom.
            Contenido del artículo: Mundo abierto ampliado, alternancia entre héroes y nuevas mecánicas.
            ¿Qué opinas de este juego? Compártenos tu experiencia en NoLimitsCorp@gmail.com.
        """.trimIndent()
    }

    if ((n.contains("spiderman 3") || n.contains("spider man 3")) && !isVideogame(n)) {
        return """
            Título completo: Spider-Man 3.
            Autor: Stan Lee y Sam Raimi.
            Fecha: 2007.
            Imagen destacada: Spider-Man con el simbionte.
            Contenido del artículo: Conflicto interno del héroe y aparición de nuevos villanos.
            ¿Qué opinas de esta entrega? Compártenos tu experiencia en NoLimitsCorp@gmail.com.
        """.trimIndent()
    }

    if ((n.contains("spiderman 2") || n.contains("spider man 2")) && !isVideogame(n)) {
        return """
            Título completo: Spider-Man 2.
            Autor: Stan Lee y Sam Raimi.
            Fecha: 2004.
            Imagen destacada: Spider-Man frente a Doctor Octopus.
            Contenido del artículo: Dilemas personales del protagonista y enfrentamiento con Doc Ock.
            ¿Qué opinas de esta entrega? Compártenos tu experiencia en NoLimitsCorp@gmail.com.
        """.trimIndent()
    }

    if ((n.contains("spiderman") || n.contains("spider man"))
        && !n.contains("2") && !n.contains("3")
        && !n.contains("remastered") && !n.contains("miles morales")
        && !isAccessory(n) && !isVideogame(n)
    ) {
        return """
            Título completo: Spider-Man.
            Autor: Stan Lee y Sam Raimi.
            Fecha: 2002.
            Imagen destacada: Spider-Man frente al Duende Verde.
            Contenido del artículo: Origen del héroe y desarrollo del lema clásico sobre la responsabilidad.
            ¿Qué opinas de esta entrega? Compártenos tu experiencia en NoLimitsCorp@gmail.com.
        """.trimIndent()
    }

    return "Descripción próximamente disponible."
}