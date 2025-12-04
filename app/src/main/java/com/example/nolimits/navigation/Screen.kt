package com.example.nolimits.navigation

/*
    Define las rutas de cada pantalla.
*/
sealed class Screen(val route: String) {
    data object SignIn   : Screen("signin")
    data object Register : Screen("registro")
    data object Recover  : Screen("recuperar_contrasenia")
    data object Summary  : Screen("resumen")

    data object Catalog  : Screen("catalog")
    data object Cart     : Screen("cart")
    data object PantallaPrincipal : Screen("pantalla_principal")

    data object MetodoPago : Screen("metodo_pago")
}