package com.example.pasar_project_bootcamp.repository

import com.example.pasar_project_bootcamp.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ProductRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val productsCollection = firestore.collection("products")

    fun getAllProducts(callback: (List<Product>) -> Unit) {
        productsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val products = documents.map { document ->
                    document.toObject(Product::class.java).copy(id = document.id)
                }
                callback(products)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun getProductsByCategory(category: String, callback: (List<Product>) -> Unit) {
        productsCollection
            .whereEqualTo("category", category)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val products = documents.map { document ->
                    document.toObject(Product::class.java).copy(id = document.id)
                }
                callback(products)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun searchProducts(query: String, callback: (List<Product>) -> Unit) {
        productsCollection
            .orderBy("name")
            .startAt(query)
            .endAt(query + "\uf8ff")
            .get()
            .addOnSuccessListener { documents ->
                val products = documents.map { document ->
                    document.toObject(Product::class.java).copy(id = document.id)
                }
                callback(products)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun addProduct(product: Product, callback: (Boolean) -> Unit) {
        productsCollection
            .add(product)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun updateProduct(productId: String, product: Product, callback: (Boolean) -> Unit) {
        productsCollection
            .document(productId)
            .set(product)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun deleteProduct(productId: String, callback: (Boolean) -> Unit) {
        productsCollection
            .document(productId)
            .delete()
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}