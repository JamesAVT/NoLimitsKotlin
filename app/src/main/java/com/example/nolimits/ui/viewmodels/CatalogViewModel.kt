package com.example.nolimits.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nolimits.data.repository.ProductRepository
import com.example.nolimits.domain.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

    private val repo = ProductRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _products.value = repo.fetchProducts()
            } catch (e: Exception) {
                _error.value = "Error al cargar productos"
            } finally {
                _loading.value = false
            }
        }
    }
}