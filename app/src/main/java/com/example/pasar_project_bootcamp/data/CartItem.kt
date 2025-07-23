package com.example.pasar_project_bootcamp.data

data class CartItem(
    val id: String = "",
    val product: Product = Product(),
    val quantity: Int = 0,
    val totalPrice: Double = 0.0
)