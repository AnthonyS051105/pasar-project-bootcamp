package com.example.pasar_project_bootcamp.data

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val farmerId: String = "",
    val farmerName: String = "",
    val location: String = "",
    val quantity: Int = 0,
    val unit: String = "kg",
    val createdAt: Long = System.currentTimeMillis()
)