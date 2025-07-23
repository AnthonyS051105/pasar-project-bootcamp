package com.example.pasar_project_bootcamp.data

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val farmerId: String = "",
    val farmerName: String = "",
    val farmerAddress: String = "",
    val category: String = "",
    val stock: Int = 0,
    val rating: Float = 0.0f,
    val totalReviews: Int = 0
)