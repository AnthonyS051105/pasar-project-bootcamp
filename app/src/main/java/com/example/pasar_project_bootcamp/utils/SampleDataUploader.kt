package com.example.pasar_project_bootcamp.utils

import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.firebase.FirebaseHelper

class SampleDataUploader {
    
    private val firebaseHelper = FirebaseHelper()
    
    fun uploadSampleProducts(onComplete: (Boolean) -> Unit) {
        val sampleProducts = listOf(
            // TukuBuah
            Product(
                name = "Apel Malang",
                description = "Apel segar berkualitas tinggi dari Malang, Jawa Timur. Rasa manis dan tekstur renyah.",
                price = 25000.0,
                imageUrl = "drawable://product_apel",
                farmerId = "farmer001",
                farmerName = "Petani Buah Sejahtera",
                farmerAddress = "Malang, Jawa Timur",
                category = "TukuBuah",
                stock = 50,
                rating = 4.8f,
                totalReviews = 25
            ),
            Product(
                name = "Jeruk Pontianak",
                description = "Jeruk manis dari Pontianak dengan kandungan vitamin C tinggi.",
                price = 20000.0,
                imageUrl = "drawable://product_jeruk",
                farmerId = "farmer002",
                farmerName = "Kebun Jeruk Manis",
                farmerAddress = "Pontianak, Kalimantan Barat",
                category = "TukuBuah",
                stock = 75,
                rating = 4.6f,
                totalReviews = 18
            ),
            Product(
                name = "Pisang Cavendish",
                description = "Pisang cavendish premium dengan rasa manis dan tekstur lembut.",
                price = 15000.0,
                imageUrl = "",
                farmerId = "farmer003",
                farmerName = "Perkebunan Pisang Nusantara",
                farmerAddress = "Lampung",
                category = "TukuBuah",
                stock = 100,
                rating = 4.7f,
                totalReviews = 32
            ),
            
            // TukuSayur
            Product(
                name = "Kangkung Organik",
                description = "Kangkung organik segar tanpa pestisida, cocok untuk tumisan dan gado-gado.",
                price = 8000.0,
                imageUrl = "drawable://product_kangkung",
                farmerId = "farmer004",
                farmerName = "Tani Organik Indonesia",
                farmerAddress = "Bogor, Jawa Barat",
                category = "TukuSayur",
                stock = 200,
                rating = 4.9f,
                totalReviews = 45
            ),
            Product(
                name = "Bayam Merah",
                description = "Bayam merah segar kaya akan zat besi dan vitamin A.",
                price = 7000.0,
                imageUrl = "",
                farmerId = "farmer005",
                farmerName = "Sayur Sehat Nusantara",
                farmerAddress = "Bandung, Jawa Barat",
                category = "TukuSayur",
                stock = 150,
                rating = 4.5f,
                totalReviews = 28
            ),
            Product(
                name = "Tomat Cherry",
                description = "Tomat cherry manis dan segar, cocok untuk salad dan garnish.",
                price = 35000.0,
                imageUrl = "",
                farmerId = "farmer006",
                farmerName = "Greenhouse Modern",
                farmerAddress = "Lembang, Jawa Barat",
                category = "TukuSayur",
                stock = 80,
                rating = 4.8f,
                totalReviews = 22
            ),
            
            // TukuBumbu
            Product(
                name = "Cabai Merah Keriting",
                description = "Cabai merah keriting segar dengan tingkat kepedasan sedang.",
                price = 45000.0,
                imageUrl = "drawable://product_cabai",
                farmerId = "farmer007",
                farmerName = "Petani Cabai Nusantara",
                farmerAddress = "Garut, Jawa Barat",
                category = "TukuBumbu",
                stock = 60,
                rating = 4.6f,
                totalReviews = 15
            ),
            Product(
                name = "Jahe Merah",
                description = "Jahe merah berkualitas tinggi untuk minuman herbal dan bumbu masakan.",
                price = 25000.0,
                imageUrl = "",
                farmerId = "farmer008",
                farmerName = "Rempah Tradisional",
                farmerAddress = "Yogyakarta",
                category = "TukuBumbu",
                stock = 40,
                rating = 4.9f,
                totalReviews = 35
            ),
            Product(
                name = "Kunyit Segar",
                description = "Kunyit segar organik untuk bumbu masakan dan jamu tradisional.",
                price = 18000.0,
                imageUrl = "",
                farmerId = "farmer009",
                farmerName = "Jamu Sehat Indonesia",
                farmerAddress = "Solo, Jawa Tengah",
                category = "TukuBumbu",
                stock = 90,
                rating = 4.7f,
                totalReviews = 20
            ),
            
            // TukuBenih
            Product(
                name = "Benih Tomat Hibrida",
                description = "Benih tomat hibrida unggul dengan daya tumbuh tinggi dan hasil melimpah.",
                price = 50000.0,
                imageUrl = "",
                farmerId = "farmer010",
                farmerName = "Benih Unggul Nusantara",
                farmerAddress = "Malang, Jawa Timur",
                category = "TukuBenih",
                stock = 25,
                rating = 4.8f,
                totalReviews = 12
            ),
            Product(
                name = "Benih Cabai Rawit",
                description = "Benih cabai rawit super pedas dengan produktivitas tinggi.",
                price = 30000.0,
                imageUrl = "",
                farmerId = "farmer011",
                farmerName = "Seed Center Indonesia",
                farmerAddress = "Bandung, Jawa Barat",
                category = "TukuBenih",
                stock = 35,
                rating = 4.6f,
                totalReviews = 8
            ),
            Product(
                name = "Benih Pakcoy",
                description = "Benih pakcoy berkualitas tinggi untuk budidaya sayuran organik.",
                price = 15000.0,
                imageUrl = "",
                farmerId = "farmer012",
                farmerName = "Organic Seeds Co",
                farmerAddress = "Bogor, Jawa Barat",
                category = "TukuBenih",
                stock = 50,
                rating = 4.7f,
                totalReviews = 16
            )
        )
        
        var uploadedCount = 0
        val totalProducts = sampleProducts.size
        
        sampleProducts.forEach { product ->
            firebaseHelper.addProduct(product) { success, productId ->
                uploadedCount++
                if (uploadedCount == totalProducts) {
                    onComplete(true)
                }
            }
        }
    }
}