package com.example.pasar_project_bootcamp.data

import java.util.Date

data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val deliveryAddress: String = "",
    val orderDate: Date = Date(),
    val status: OrderStatus = OrderStatus.PENDING,
    val paymentMethod: String = ""
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}