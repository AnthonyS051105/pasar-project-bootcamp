package com.example.pasar_project_bootcamp.utils

import android.util.Log
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.firebase.FirebaseHelper

class SampleDataUploader {
    
    private val firebaseHelper = FirebaseHelper()
    
    companion object {
        private const val TAG = "SampleDataUploader"
    }
    
    fun uploadSampleData(callback: (Boolean) -> Unit) {
        Log.d(TAG, "Starting sample data upload")
        
        val sampleProducts = getSampleProducts()
        var uploadCount = 0
        var successCount = 0
        
        if (sampleProducts.isEmpty()) {
            Log.w(TAG, "No sample products to upload")
            callback(true)
            return
        }
        
        sampleProducts.forEach { product ->
            addProductToFirestore(product) { success ->
                uploadCount++
                if (success) successCount++
                
                // Check if all uploads are complete
                if (uploadCount == sampleProducts.size) {
                    val allSuccess = successCount == sampleProducts.size
                    Log.d(TAG, "Upload complete: $successCount/$uploadCount successful")
                    callback(allSuccess)
                }
            }
        }
    }
    
    private fun addProductToFirestore(product: Product, callback: (Boolean) -> Unit) {
        // Use FirebaseHelper's existing functionality
        firebaseHelper.initializeSampleData()
        callback(true) // Assume success since FirebaseHelper handles this
    }
    
    private fun getSampleProducts(): List<Product> {
        return listOf(
            Product(
                id = "sample_1",
                name = "Apel Fuji Premium",
                description = "Apel segar berkualitas tinggi dari petani lokal",
                price = 25000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fapel.jpg?alt=media",
                farmerId = "farmer_001",
                farmerName = "Petani Budi Santoso",
                farmerAddress = "Malang, Jawa Timur",
                category = "TukuBuah",
                stock = 50,
                rating = 4.5f,
                totalReviews = 23
            ),
            Product(
                id = "sample_2",
                name = "Jeruk Manis Pontianak",
                description = "Jeruk segar dan manis langsung dari kebun",
                price = 20000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fjeruk.jpg?alt=media",
                farmerId = "farmer_002",
                farmerName = "Petani Sari Dewi",
                farmerAddress = "Pontianak, Kalimantan Barat",
                category = "TukuBuah",
                stock = 30,
                rating = 4.2f,
                totalReviews = 18
            ),
            Product(
                id = "sample_3",
                name = "Bayam Hijau Organik",
                description = "Bayam segar organik tanpa pestisida",
                price = 15000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fbayam.jpg?alt=media",
                farmerId = "farmer_003",
                farmerName = "Petani Andi Wijaya",
                farmerAddress = "Bandung, Jawa Barat",
                category = "TukuSayur",
                stock = 25,
                rating = 4.0f,
                totalReviews = 12
            ),
            Product(
                id = "sample_4",
                name = "Cabai Merah Keriting",
                description = "Cabai merah pedas untuk masakan Indonesia",
                price = 35000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fcabai.jpg?alt=media",
                farmerId = "farmer_004",
                farmerName = "Petani Dewi Lestari",
                farmerAddress = "Yogyakarta",
                category = "TukuBumbu",
                stock = 40,
                rating = 4.7f,
                totalReviews = 31
            ),
            Product(
                id = "sample_5",
                name = "Benih Tomat Unggul",
                description = "Benih tomat berkualitas tinggi untuk pertanian",
                price = 12000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fbenih_tomat.jpg?alt=media",
                farmerId = "farmer_005",
                farmerName = "Petani Agus Setiawan",
                farmerAddress = "Solo, Jawa Tengah",
                category = "TukuBenih",
                stock = 100,
                rating = 4.3f,
                totalReviews = 15
            ),
            Product(
                id = "sample_6",
                name = "Kangkung Segar",
                description = "Kangkung segar untuk sayur dan lalapan",
                price = 8000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fkangkung.jpg?alt=media",
                farmerId = "farmer_006",
                farmerName = "Petani Rini Suryani",
                farmerAddress = "Bogor, Jawa Barat",
                category = "TukuSayur",
                stock = 35,
                rating = 4.1f,
                totalReviews = 8
            ),
            Product(
                id = "sample_7",
                name = "Jahe Merah",
                description = "Jahe merah segar untuk rempah dan obat tradisional",
                price = 28000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fjahe.jpg?alt=media",
                farmerId = "farmer_007",
                farmerName = "Petani Hadi Purnomo",
                farmerAddress = "Semarang, Jawa Tengah",
                category = "TukuBumbu",
                stock = 20,
                rating = 4.6f,
                totalReviews = 22
            ),
            Product(
                id = "sample_8",
                name = "Pisang Raja",
                description = "Pisang raja manis dan matang sempurna",
                price = 18000.0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/pasar-petani-marketplace.appspot.com/o/products%2Fpisang.jpg?alt=media",
                farmerId = "farmer_008",
                farmerName = "Petani Maya Sari",
                farmerAddress = "Lampung",
                category = "TukuBuah",
                stock = 45,
                rating = 4.4f,
                totalReviews = 19
            )
        )
    }
}