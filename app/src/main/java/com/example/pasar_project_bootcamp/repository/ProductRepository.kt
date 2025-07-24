package com.example.pasar_project_bootcamp.repository

import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.firebase.FirebaseHelper

class ProductRepository {

    private val firebaseHelper = FirebaseHelper()

    // Get all products
    fun getAllProducts(callback: (List<Product>) -> Unit) {
        firebaseHelper.getProducts(callback)
    }

    // Get products by category
    fun getProductsByCategory(category: String, callback: (List<Product>) -> Unit) {
        firebaseHelper.getProductsByCategory(category, callback)
    }

    // Get product by ID
    fun getProductById(productId: String, callback: (Product?) -> Unit) {
        firebaseHelper.getProductById(productId, callback)
    }

    // Search products locally (after getting from Firebase)
    fun searchProducts(query: String, callback: (List<Product>) -> Unit) {
        firebaseHelper.getProducts { products ->
            val filteredProducts = products.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true) ||
                        product.category.contains(query, ignoreCase = true) ||
                        product.farmerName.contains(query, ignoreCase = true)
            }
            callback(filteredProducts)
        }
    }

    // Initialize sample data
    fun initializeSampleData() {
        firebaseHelper.initializeSampleData()
    }
}