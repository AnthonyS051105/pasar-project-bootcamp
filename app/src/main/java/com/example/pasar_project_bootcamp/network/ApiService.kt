package com.example.pasar_project_bootcamp.network

import com.example.pasar_project_bootcamp.data.api.ApiResponse
import com.example.pasar_project_bootcamp.data.api.ProductApiModel
import com.example.pasar_project_bootcamp.data.api.UserApiModel
import com.example.pasar_project_bootcamp.data.api.OrderApiModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Products Endpoints
    @GET("products")
    suspend fun getAllProducts(): Response<ApiResponse<List<ProductApiModel>>>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String
    ): Response<ApiResponse<List<ProductApiModel>>>

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): Response<ApiResponse<List<ProductApiModel>>>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") productId: String
    ): Response<ApiResponse<ProductApiModel>>

    @POST("products")
    suspend fun createProduct(
        @Body product: ProductApiModel
    ): Response<ApiResponse<ProductApiModel>>

    // User Endpoints
    @GET("users/{id}")
    suspend fun getUserProfile(
        @Path("id") userId: String
    ): Response<ApiResponse<UserApiModel>>

    @PUT("users/{id}")
    suspend fun updateUserProfile(
        @Path("id") userId: String,
        @Body user: UserApiModel
    ): Response<ApiResponse<UserApiModel>>

    // Orders Endpoints
    @GET("orders/user/{userId}")
    suspend fun getUserOrders(
        @Path("userId") userId: String
    ): Response<ApiResponse<List<OrderApiModel>>>

    @POST("orders")
    suspend fun createOrder(
        @Body order: OrderApiModel
    ): Response<ApiResponse<OrderApiModel>>

    @GET("orders/{id}")
    suspend fun getOrderById(
        @Path("id") orderId: String
    ): Response<ApiResponse<OrderApiModel>>

    // Cart Endpoints (jika menggunakan server-side cart)
    @GET("users/{userId}/cart")
    suspend fun getUserCart(
        @Path("userId") userId: String
    ): Response<ApiResponse<List<ProductApiModel>>>

    @POST("users/{userId}/cart")
    suspend fun addToCart(
        @Path("userId") userId: String,
        @Body item: Map<String, Any>
    ): Response<ApiResponse<String>>

    @DELETE("users/{userId}/cart/{productId}")
    suspend fun removeFromCart(
        @Path("userId") userId: String,
        @Path("productId") productId: String
    ): Response<ApiResponse<String>>
}