package com.example.pasar_project_bootcamp.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.FirebaseAuth
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.data.Order
import com.example.pasar_project_bootcamp.data.CartItem

class FirebaseHelper {
    
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
    // Products Collection
    fun getProducts(callback: (List<Product>) -> Unit) {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                val products = documents.map { doc ->
                    doc.toObject(Product::class.java).copy(id = doc.id)
                }
                callback(products)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
    
    fun getProductById(productId: String, callback: (Product?) -> Unit) {
        firestore.collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val product = document.toObject(Product::class.java)?.copy(id = document.id)
                    callback(product)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
    
    // Orders Collection
    fun saveOrder(order: Order, callback: (Boolean) -> Unit) {
        firestore.collection("orders")
            .document(order.id)
            .set(order)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
    
    fun getUserOrders(userId: String, callback: (List<Order>) -> Unit) {
        firestore.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val orders = documents.map { doc ->
                    doc.toObject(Order::class.java).copy(id = doc.id)
                }
                callback(orders)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
    
    // Cart Operations (stored per user)
    fun saveCartItem(userId: String, cartItem: CartItem, callback: (Boolean) -> Unit) {
        firestore.collection("users")
            .document(userId)
            .collection("cart")
            .document(cartItem.id)
            .set(cartItem)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
    
    fun getUserCart(userId: String, callback: (List<CartItem>) -> Unit) {
        firestore.collection("users")
            .document(userId)
            .collection("cart")
            .get()
            .addOnSuccessListener { documents ->
                val cartItems = documents.map { doc ->
                    doc.toObject(CartItem::class.java).copy(id = doc.id)
                }
                callback(cartItems)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
    
    fun clearUserCart(userId: String, callback: (Boolean) -> Unit) {
        firestore.collection("users")
            .document(userId)
            .collection("cart")
            .get()
            .addOnSuccessListener { documents ->
                val batch = firestore.batch()
                documents.forEach { doc ->
                    batch.delete(doc.reference)
                }
                batch.commit()
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
            .addOnFailureListener {
                callback(false)
            }
    }
    
    // Authentication
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
    
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}