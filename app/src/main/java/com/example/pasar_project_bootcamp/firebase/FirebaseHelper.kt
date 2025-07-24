package com.example.pasar_project_bootcamp.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.data.Order
import com.example.pasar_project_bootcamp.data.CartItem

class FirebaseHelper {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    companion object {
        private const val TAG = "FirebaseHelper"
    }

    // Initialize sample data if collection is empty
    fun initializeSampleData() {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d(TAG, "Products collection is empty, adding sample data")
                    addSampleProducts()
                } else {
                    Log.d(TAG, "Products collection already has ${documents.size()} items")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error checking products collection", exception)
            }
    }

    private fun addSampleProducts() {
        val sampleProducts = listOf(
            Product(
                id = "1",
                name = "Apel Fuji",
                description = "Apel segar dari petani lokal",
                price = 25000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fapel.jpg?alt=media",
                farmerId = "farmer1",
                farmerName = "Petani Budi",
                farmerAddress = "Malang, Jawa Timur",
                category = "TukuBuah",
                stock = 50,
                rating = 4.5f,
                totalReviews = 23
            ),
            Product(
                id = "2",
                name = "Jeruk Manis",
                description = "Jeruk segar dan manis",
                price = 20000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fjeruk.jpg?alt=media",
                farmerId = "farmer2",
                farmerName = "Petani Sari",
                farmerAddress = "Pontianak, Kalimantan Barat",
                category = "TukuBuah",
                stock = 30,
                rating = 4.2f,
                totalReviews = 18
            ),
            Product(
                id = "3",
                name = "Bayam Hijau",
                description = "Bayam segar organik",
                price = 15000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fbayam.jpg?alt=media",
                farmerId = "farmer3",
                farmerName = "Petani Andi",
                farmerAddress = "Bandung, Jawa Barat",
                category = "TukuSayur",
                stock = 25,
                rating = 4.0f,
                totalReviews = 12
            ),
            Product(
                id = "4",
                name = "Cabai Merah",
                description = "Cabai merah pedas untuk masakan",
                price = 35000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fcabai.jpg?alt=media",
                farmerId = "farmer4",
                farmerName = "Petani Dewi",
                farmerAddress = "Yogyakarta",
                category = "TukuBumbu",
                stock = 40,
                rating = 4.7f,
                totalReviews = 31
            )
        )

        sampleProducts.forEach { product ->
            firestore.collection("products")
                .document(product.id)
                .set(product)
                .addOnSuccessListener {
                    Log.d(TAG, "Sample product added: ${product.name}")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error adding sample product: ${product.name}", exception)
                }
        }
    }

    // Products Collection
    fun getProducts(callback: (List<Product>) -> Unit) {
        Log.d(TAG, "Fetching products from Firestore")
        firestore.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                val products = documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Product::class.java).copy(id = doc.id)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing product document: ${doc.id}", e)
                        null
                    }
                }
                Log.d(TAG, "Successfully fetched ${products.size} products")
                callback(products)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching products", exception)
                callback(emptyList())
            }
    }

    fun getProductsByCategory(category: String, callback: (List<Product>) -> Unit) {
        Log.d(TAG, "Fetching products for category: $category")
        firestore.collection("products")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { documents ->
                val products = documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Product::class.java).copy(id = doc.id)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing product document: ${doc.id}", e)
                        null
                    }
                }
                Log.d(TAG, "Successfully fetched ${products.size} products for category $category")
                callback(products)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching products for category $category", exception)
                callback(emptyList())
            }
    }

    fun getProductById(productId: String, callback: (Product?) -> Unit) {
        Log.d(TAG, "Fetching product with ID: $productId")
        firestore.collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    try {
                        val product = document.toObject(Product::class.java)?.copy(id = document.id)
                        Log.d(TAG, "Successfully fetched product: ${product?.name}")
                        callback(product)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing product document: $productId", e)
                        callback(null)
                    }
                } else {
                    Log.w(TAG, "Product not found: $productId")
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching product: $productId", exception)
                callback(null)
            }
    }

    // Orders Collection
    fun saveOrder(order: Order, callback: (Boolean) -> Unit) {
        Log.d(TAG, "Saving order: ${order.id}")
        firestore.collection("orders")
            .document(order.id)
            .set(order)
            .addOnSuccessListener {
                Log.d(TAG, "Order saved successfully: ${order.id}")
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error saving order: ${order.id}", exception)
                callback(false)
            }
    }

    fun getUserOrders(userId: String, callback: (List<Order>) -> Unit) {
        Log.d(TAG, "Fetching orders for user: $userId")
        firestore.collection("orders")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val orders = documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Order::class.java).copy(id = doc.id)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing order document: ${doc.id}", e)
                        null
                    }
                }
                Log.d(TAG, "Successfully fetched ${orders.size} orders for user $userId")
                callback(orders)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching orders for user $userId", exception)
                callback(emptyList())
            }
    }

    // Cart Operations (stored per user)
    fun saveCartItem(userId: String, cartItem: CartItem, callback: (Boolean) -> Unit) {
        Log.d(TAG, "Saving cart item for user: $userId")
        firestore.collection("users")
            .document(userId)
            .collection("cart")
            .document(cartItem.id)
            .set(cartItem)
            .addOnSuccessListener {
                Log.d(TAG, "Cart item saved successfully")
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error saving cart item", exception)
                callback(false)
            }
    }

    fun getUserCart(userId: String, callback: (List<CartItem>) -> Unit) {
        Log.d(TAG, "Fetching cart for user: $userId")
        firestore.collection("users")
            .document(userId)
            .collection("cart")
            .get()
            .addOnSuccessListener { documents ->
                val cartItems = documents.mapNotNull { doc ->
                    try {
                        doc.toObject(CartItem::class.java).copy(id = doc.id)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing cart item document: ${doc.id}", e)
                        null
                    }
                }
                Log.d(TAG, "Successfully fetched ${cartItems.size} cart items for user $userId")
                callback(cartItems)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching cart for user $userId", exception)
                callback(emptyList())
            }
    }

    fun clearUserCart(userId: String, callback: (Boolean) -> Unit) {
        Log.d(TAG, "Clearing cart for user: $userId")
        firestore.collection("users")
            .document(userId)
            .collection("cart")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d(TAG, "Cart is already empty for user $userId")
                    callback(true)
                    return@addOnSuccessListener
                }

                val batch = firestore.batch()
                documents.forEach { doc ->
                    batch.delete(doc.reference)
                }
                batch.commit()
                    .addOnSuccessListener {
                        Log.d(TAG, "Cart cleared successfully for user $userId")
                        callback(true)
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error clearing cart for user $userId", exception)
                        callback(false)
                    }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error accessing cart for user $userId", exception)
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

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
}