package com.example.pasar_project_bootcamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pasar_project_bootcamp.data.CartItem
import com.example.pasar_project_bootcamp.data.CartManager
import com.example.pasar_project_bootcamp.data.Product
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {

    val items: StateFlow<List<CartItem>> = CartManager.items

    fun addItem(product: Product, quantity: Int = 1) {
        CartManager.addItem(product, quantity)
    }

    fun removeItem(cartItemId: String) {
        CartManager.removeItem(cartItemId)
    }

    fun updateQuantity(cartItemId: String, quantity: Int) {
        CartManager.updateQuantity(cartItemId, quantity)
    }

    fun clear() {
        CartManager.clear()
    }

    fun getTotal(): Double = CartManager.getTotal()
}
