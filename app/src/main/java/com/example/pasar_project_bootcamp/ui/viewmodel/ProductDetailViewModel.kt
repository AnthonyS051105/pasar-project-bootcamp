package com.example.pasar_project_bootcamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pasar_project_bootcamp.data.CartManager
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductDetailViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _notFound = MutableStateFlow(false)
    val notFound: StateFlow<Boolean> = _notFound.asStateFlow()

    fun loadProduct(productId: String) {
        productRepository.getProductById(productId) { result ->
            if (result == null) {
                _notFound.value = true
            } else {
                _product.value = result
            }
        }
    }

    fun addToCart(quantity: Int = 1) {
        val current = _product.value ?: return
        CartManager.addItem(current, quantity)
    }
}
