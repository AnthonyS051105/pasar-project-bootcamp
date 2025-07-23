package com.example.pasar_project_bootcamp.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.auth.FirebaseAuth
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.data.Order
import com.example.pasar_project_bootcamp.data.CartItem
import java.util.UUID

class FirebaseHelper {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    companion object {
        private const val COLLECTION_PRODUCTS = "products"
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_ORDERS = "orders"
        private const val COLLECTION_CART = "cart"
    }

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

    fun signInWithEmail(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun signUpWithEmail(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }

    // Enhanced Products Functions
    fun getProductsByCategory(category: String, callback: (List<Product>) -> Unit) {
        firestore.collection(COLLECTION_PRODUCTS)
            .whereEqualTo("category", category)
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

    fun searchProducts(query: String, callback: (List<Product>) -> Unit) {
        firestore.collection(COLLECTION_PRODUCTS)
            .orderBy("name")
            .startAt(query)
            .endAt(query + "\uf8ff")
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

    fun addProduct(product: Product, callback: (Boolean, String?) -> Unit) {
        firestore.collection(COLLECTION_PRODUCTS)
            .add(product)
            .addOnSuccessListener { documentReference ->
                callback(true, documentReference.id)
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message)
            }
    }

    // Note: Image upload removed - using local drawable resources instead
    // If you need image upload later, you can use third-party services like:
    // - Cloudinary (free tier available)
    // - ImgBB API
    // - Your own server

    // User Profile
    fun saveUserProfile(userId: String, userData: Map<String, Any>, callback: (Boolean) -> Unit) {
        firestore.collection(COLLECTION_USERS)
            .document(userId)
            .set(userData)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun getUserProfile(userId: String, callback: (Map<String, Any>?) -> Unit) {
        firestore.collection(COLLECTION_USERS)
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    callback(document.data)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}