package com.example.pasar_project_bootcamp.repository

import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.data.api.ProductApiModel
import com.example.pasar_project_bootcamp.firebase.FirebaseHelper
import com.example.pasar_project_bootcamp.network.MockApiModule
import com.example.pasar_project_bootcamp.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {
    
    private val firebaseHelper = FirebaseHelper()
    
    // Toggle antara Mock API dan Real API
    private val apiService = MockApiModule.apiService // Ganti ke NetworkModule.apiService untuk real API
    
    // Data source priority: API first, then Firebase fallback
    suspend fun getProductsByCategory(category: String): List<Product> {
        return withContext(Dispatchers.IO) {
            try {
                // Try API first
                val response = apiService.getProductsByCategory(category)
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.map { it.toProduct() } ?: emptyList()
                } else {
                    // Fallback to Firebase
                    getFirebaseProductsByCategory(category)
                }
            } catch (e: Exception) {
                // If API fails, fallback to Firebase
                getFirebaseProductsByCategory(category)
            }
        }
    }
    
    suspend fun searchProducts(query: String): List<Product> {
        return withContext(Dispatchers.IO) {
            try {
                // Try API first
                val response = apiService.searchProducts(query)
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.map { it.toProduct() } ?: emptyList()
                } else {
                    // Fallback to Firebase
                    getFirebaseSearchResults(query)
                }
            } catch (e: Exception) {
                // If API fails, fallback to Firebase
                getFirebaseSearchResults(query)
            }
        }
    }
    
    suspend fun getAllProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            try {
                // Try API first
                val response = apiService.getAllProducts()
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.map { it.toProduct() } ?: emptyList()
                } else {
                    // Fallback to Firebase
                    getFirebaseAllProducts()
                }
            } catch (e: Exception) {
                // If API fails, fallback to Firebase
                getFirebaseAllProducts()
            }
        }
    }
    
    suspend fun getProductById(productId: String): Product? {
        return withContext(Dispatchers.IO) {
            try {
                // Try API first
                val response = apiService.getProductById(productId)
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.toProduct()
                } else {
                    // Fallback to Firebase
                    getFirebaseProductById(productId)
                }
            } catch (e: Exception) {
                // If API fails, fallback to Firebase
                getFirebaseProductById(productId)
            }
        }
    }
    
    // Firebase fallback methods
    private suspend fun getFirebaseProductsByCategory(category: String): List<Product> {
        return withContext(Dispatchers.IO) {
            val result = mutableListOf<Product>()
            firebaseHelper.getProductsByCategory(category) { products ->
                result.addAll(products)
            }
            // Wait for Firebase callback (in real implementation, use coroutines properly)
            Thread.sleep(1000)
            result.toList()
        }
    }
    
    private suspend fun getFirebaseSearchResults(query: String): List<Product> {
        return withContext(Dispatchers.IO) {
            val result = mutableListOf<Product>()
            firebaseHelper.searchProducts(query) { products ->
                result.addAll(products)
            }
            Thread.sleep(1000)
            result.toList()
        }
    }
    
    private suspend fun getFirebaseAllProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            val result = mutableListOf<Product>()
            firebaseHelper.getProducts { products ->
                result.addAll(products)
            }
            Thread.sleep(1000)
            result.toList()
        }
    }
    
    private suspend fun getFirebaseProductById(productId: String): Product? {
        return withContext(Dispatchers.IO) {
            var result: Product? = null
            firebaseHelper.getProductById(productId) { product ->
                result = product
            }
            Thread.sleep(1000)
            result
        }
    }
    
    // Sync data: Save API data to Firebase for offline access
    suspend fun syncApiDataToFirebase() {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllProducts()
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.forEach { apiProduct ->
                        val product = apiProduct.toProduct()
                        firebaseHelper.addProduct(product) { _, _ -> }
                    }
                }
            } catch (e: Exception) {
                // Handle sync error
            }
        }
    }
}

// Extension function to convert API model to local model
private fun ProductApiModel.toProduct(): Product {
    return Product(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        imageUrl = this.imageUrl,
        farmerId = "api_farmer_${this.id}",
        farmerName = this.farmerName,
        farmerAddress = this.farmerAddress,
        category = this.category,
        stock = this.stock,
        rating = this.rating,
        totalReviews = 0 // API doesn't provide this, use default
    )
}