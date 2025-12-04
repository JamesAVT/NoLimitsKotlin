package com.example.nolimits.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.nolimits.navigation.NavigationEvent
import com.example.nolimits.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/* 
    Puede controlar el estado global de la app.
    Valores compartidos entre pantallas.
    Generalmente usado para menús, navbars, etc.
*/

class MainViewModel : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    // ‘receiveAsFlow()’: Expone el Channel como un Flow de solo lectura para que la UI pueda observarlo.
    // Esto previene que la UI pueda enviar eventos al Channel directamente.

    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()
    // Función que emite el evento de navegación hacia la ruta deseada.
    fun navigateTo(screen: Screen) {
        CoroutineScope(context = Dispatchers.Main).launch {
            _navigationEvents.emit(value = NavigationEvent.NavigateTo(route = screen))
        }
    }

    // Función para volver atrás
    fun navigateBack() {
        CoroutineScope(context = Dispatchers.Main).launch {
            _navigationEvents.emit(value = NavigationEvent.PopBackStack)
        }
    }

    // Función para navegar hacia arriba (padre)
    fun navigateUp() {
        CoroutineScope(context = Dispatchers.Main).launch {
            _navigationEvents.emit(value = NavigationEvent.NavigateUp)
        }
    }
}
