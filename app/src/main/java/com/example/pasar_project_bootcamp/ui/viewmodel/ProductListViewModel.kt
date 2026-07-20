package com.example.pasar_project_bootcamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductListViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isEmpty = MutableStateFlow(false)
    val isEmpty: StateFlow<Boolean> = _isEmpty.asStateFlow()

    fun load(category: String, searchQuery: String) {
        when {
            searchQuery.isNotEmpty() -> {
                productRepository.searchProducts(searchQuery) { onLoaded(it) }
            }
            category.isNotEmpty() -> {
                productRepository.getProductsByCategory(category) { onLoaded(it) }
            }
            else -> {
                productRepository.getAllProducts { onLoaded(it) }
            }
        }
    }

    fun filterLocally(query: String) {
        // Client-side refine over currently loaded list is handled by Fragment
        // since the raw list isn't retained separately here; kept minimal per MVP scope.
    }

    private fun onLoaded(products: List<Product>) {
        _products.value = products
        _isEmpty.value = products.isEmpty()
    }
}
