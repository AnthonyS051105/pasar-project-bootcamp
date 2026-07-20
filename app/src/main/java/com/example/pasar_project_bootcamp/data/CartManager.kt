package com.example.pasar_project_bootcamp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object CartManager {

    private val cartItems = mutableListOf<CartItem>()
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    fun addItem(product: Product, quantity: Int) {
        val existingIndex = cartItems.indexOfFirst { it.product.id == product.id }
        if (existingIndex >= 0) {
            val existing = cartItems[existingIndex]
            val newQuantity = existing.quantity + quantity
            cartItems[existingIndex] = existing.copy(
                quantity = newQuantity,
                totalPrice = product.price * newQuantity
            )
        } else {
            cartItems.add(
                CartItem(
                    id = product.id,
                    product = product,
                    quantity = quantity,
                    totalPrice = product.price * quantity
                )
            )
        }
        publish()
    }

    fun removeItem(cartItemId: String) {
        cartItems.removeAll { it.id == cartItemId }
        publish()
    }

    fun updateQuantity(cartItemId: String, quantity: Int) {
        val index = cartItems.indexOfFirst { it.id == cartItemId }
        if (index < 0) return
        if (quantity <= 0) {
            cartItems.removeAt(index)
        } else {
            val existing = cartItems[index]
            cartItems[index] = existing.copy(
                quantity = quantity,
                totalPrice = existing.product.price * quantity
            )
        }
        publish()
    }

    fun clear() {
        cartItems.clear()
        publish()
    }

    fun getTotal(): Double = cartItems.sumOf { it.totalPrice }

    private fun publish() {
        _items.value = cartItems.toList()
    }
}
