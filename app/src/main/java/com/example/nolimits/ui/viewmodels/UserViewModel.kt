package com.example.nolimits.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nolimits.data.local.dao.UserDao
import com.example.nolimits.data.local.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/*
    Este ViewModel trabaja con Room:
    - Guarda usuarios en la base de datos.
    - Lee usuarios desde Room.
    - Expone un StateFlow con la lista de usuarios.
    - Maneja lógica CRUD real (persistencia).

    Maneja la lógica del perfil de usuario.
    Editar datos.
    Validaciones.
    Actualizar en repositorio.
    Recuperar sesión guardada.
*/

class UserViewModel(private val dao: UserDao) : ViewModel() {

    val users = dao.getAllUsers()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addUser(name: String, age: Int) {
        viewModelScope.launch {
            dao.addUser(
                User(
                    name = name,
                    age = age
                )
            )
        }
    }
}
