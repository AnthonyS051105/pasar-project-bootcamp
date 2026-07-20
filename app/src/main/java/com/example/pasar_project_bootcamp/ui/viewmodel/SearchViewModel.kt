package com.example.pasar_project_bootcamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _recommendations = MutableStateFlow<List<Product>>(emptyList())
    val recommendations: StateFlow<List<Product>> = _recommendations.asStateFlow()

    fun loadRecommendations() {
        productRepository.getAllProducts { products ->
            _recommendations.value = products.take(8)
        }
    }
}
