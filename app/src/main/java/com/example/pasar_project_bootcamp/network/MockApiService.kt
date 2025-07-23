package com.example.pasar_project_bootcamp.network

import com.example.pasar_project_bootcamp.data.api.ApiResponse
import com.example.pasar_project_bootcamp.data.api.ProductApiModel
import com.example.pasar_project_bootcamp.data.api.UserApiModel
import com.example.pasar_project_bootcamp.data.api.OrderApiModel
import kotlinx.coroutines.delay
import retrofit2.Response

class MockApiService : ApiService {
    
    // Mock data untuk testing
    private val mockProducts = listOf(
        ProductApiModel(
            id = "1",
            name = "Apel Malang Premium",
            description = "Apel segar berkualitas tinggi dari Malang",
            price = 25000.0,
            imageUrl = "drawable://product_apel",
            category = "TukuBuah",
            stock = 50,
            rating = 4.8f,
            farmerName = "Petani Buah Sejahtera",
            farmerAddress = "Malang, Jawa Timur"
        ),
        ProductApiModel(
            id = "2",
            name = "Jeruk Pontianak Manis",
            description = "Jeruk manis dari Pontianak dengan vitamin C tinggi",
            price = 20000.0,
            imageUrl = "drawable://product_jeruk",
            category = "TukuBuah",
            stock = 75,
            rating = 4.6f,
            farmerName = "Kebun Jeruk Manis",
            farmerAddress = "Pontianak, Kalimantan Barat"
        ),
        ProductApiModel(
            id = "3",
            name = "Kangkung Organik Segar",
            description = "Kangkung organik tanpa pestisida",
            price = 8000.0,
            imageUrl = "drawable://product_kangkung",
            category = "TukuSayur",
            stock = 200,
            rating = 4.9f,
            farmerName = "Tani Organik Indonesia",
            farmerAddress = "Bogor, Jawa Barat"
        ),
        ProductApiModel(
            id = "4",
            name = "Cabai Merah Keriting",
            description = "Cabai merah segar tingkat kepedasan sedang",
            price = 45000.0,
            imageUrl = "drawable://product_cabai",
            category = "TukuBumbu",
            stock = 60,
            rating = 4.6f,
            farmerName = "Petani Cabai Nusantara",
            farmerAddress = "Garut, Jawa Barat"
        )
    )
    
    override suspend fun getAllProducts(): Response<ApiResponse<List<ProductApiModel>>> {
        delay(1000) // Simulate network delay
        return Response.success(
            ApiResponse(
                success = true,
                message = "Products retrieved successfully",
                data = mockProducts
            )
        )
    }
    
    override suspend fun getProductsByCategory(category: String): Response<ApiResponse<List<ProductApiModel>>> {
        delay(800)
        val filteredProducts = mockProducts.filter { it.category == category }
        return Response.success(
            ApiResponse(
                success = true,
                message = "Products retrieved successfully",
                data = filteredProducts
            )
        )
    }
    
    override suspend fun searchProducts(query: String): Response<ApiResponse<List<ProductApiModel>>> {
        delay(600)
        val searchResults = mockProducts.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.description.contains(query, ignoreCase = true)
        }
        return Response.success(
            ApiResponse(
                success = true,
                message = "Search completed successfully",
                data = searchResults
            )
        )
    }
    
    override suspend fun getProductById(productId: String): Response<ApiResponse<ProductApiModel>> {
        delay(500)
        val product = mockProducts.find { it.id == productId }
        return if (product != null) {
            Response.success(
                ApiResponse(
                    success = true,
                    message = "Product found",
                    data = product
                )
            )
        } else {
            Response.success(
                ApiResponse(
                    success = false,
                    message = "Product not found",
                    data = null
                )
            )
        }
    }
    
    override suspend fun createProduct(product: ProductApiModel): Response<ApiResponse<ProductApiModel>> {
        delay(1000)
        return Response.success(
            ApiResponse(
                success = true,
                message = "Product created successfully",
                data = product.copy(id = "new_${System.currentTimeMillis()}")
            )
        )
    }
    
    // User endpoints (mock implementation)
    override suspend fun getUserProfile(userId: String): Response<ApiResponse<UserApiModel>> {
        delay(500)
        return Response.success(
            ApiResponse(
                success = true,
                message = "User profile retrieved",
                data = UserApiModel(
                    id = userId,
                    name = "John Doe",
                    email = "john@example.com",
                    phone = "081234567890"
                )
            )
        )
    }
    
    override suspend fun updateUserProfile(userId: String, user: UserApiModel): Response<ApiResponse<UserApiModel>> {
        delay(800)
        return Response.success(
            ApiResponse(
                success = true,
                message = "Profile updated successfully",
                data = user
            )
        )
    }
    
    // Order endpoints (mock implementation)
    override suspend fun getUserOrders(userId: String): Response<ApiResponse<List<OrderApiModel>>> {
        delay(700)
        return Response.success(
            ApiResponse(
                success = true,
                message = "Orders retrieved successfully",
                data = emptyList()
            )
        )
    }
    
    override suspend fun createOrder(order: OrderApiModel): Response<ApiResponse<OrderApiModel>> {
        delay(1200)
        return Response.success(
            ApiResponse(
                success = true,
                message = "Order created successfully",
                data = order.copy(id = "order_${System.currentTimeMillis()}")
            )
        )
    }
    
    override suspend fun getOrderById(orderId: String): Response<ApiResponse<OrderApiModel>> {
        delay(500)
        return Response.success(
            ApiResponse(
                success = false,
                message = "Order not found",
                data = null
            )
        )
    }
    
    // Cart endpoints (mock implementation)
    override suspend fun getUserCart(userId: String): Response<ApiResponse<List<ProductApiModel>>> {
        delay(400)
        return Response.success(
            ApiResponse(
                success = true,
                message = "Cart retrieved successfully",
                data = emptyList()
            )
        )
    }
    
    override suspend fun addToCart(userId: String, item: Map<String, Any>): Response<ApiResponse<String>> {
        delay(600)
        return Response.success(
            ApiResponse(
                success = true,
                message = "Item added to cart",
                data = "success"
            )
        )
    }
    
    override suspend fun removeFromCart(userId: String, productId: String): Response<ApiResponse<String>> {
        delay(400)
        return Response.success(
            ApiResponse(
                success = true,
                message = "Item removed from cart",
                data = "success"
            )
        )
    }
}