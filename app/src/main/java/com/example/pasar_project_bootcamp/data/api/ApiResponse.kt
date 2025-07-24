package com.example.pasar_project_bootcamp.data.api

import com.google.gson.annotations.SerializedName

// Generic API Response
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T?
)

// Product API Model (sesuaikan dengan API yang akan digunakan)
data class ProductApiModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("farmer_name")
    val farmerName: String,
    @SerializedName("farmer_address")
    val farmerAddress: String
)

// User API Model
data class UserApiModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String
)

// Order API Model
data class OrderApiModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("total_amount")
    val totalAmount: Double,
    @SerializedName("status")
    val status: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("items")
    val items: List<OrderItemApiModel>
)

data class OrderItemApiModel(
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("price")
    val price: Double
)