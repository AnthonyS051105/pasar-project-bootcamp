package com.example.pasar_project_bootcamp.utils

import com.example.pasar_project_bootcamp.R

object ProductImageHelper {
    
    private val productImageMap = mapOf(
        // Buah-buahan
        "apel" to R.drawable.product_apel,
        "jeruk" to R.drawable.product_jeruk,
        "pisang" to R.drawable.ic_store, // fallback to store icon
        "mangga" to R.drawable.ic_store,
        "buah" to R.drawable.product_apel, // default untuk kategori buah
        
        // Sayuran
        "kangkung" to R.drawable.product_kangkung,
        "bayam" to R.drawable.product_kangkung, // similar green leafy
        "tomat" to R.drawable.product_apel, // red color similar
        "sayur" to R.drawable.product_kangkung, // default untuk kategori sayur
        
        // Bumbu
        "cabai" to R.drawable.product_cabai,
        "jahe" to R.drawable.ic_store,
        "kunyit" to R.drawable.ic_store,
        "bumbu" to R.drawable.product_cabai, // default untuk kategori bumbu
        
        // Benih
        "benih" to R.drawable.ic_store, // default untuk semua benih
        "seed" to R.drawable.ic_store
    )
    
    fun getProductImageResource(productName: String, category: String = ""): Int {
        val name = productName.lowercase()
        
        // Cari berdasarkan nama produk
        for ((key, drawable) in productImageMap) {
            if (name.contains(key)) {
                return drawable
            }
        }
        
        // Fallback berdasarkan kategori
        return when (category.lowercase()) {
            "tukubuah" -> R.drawable.product_apel
            "tukusayur" -> R.drawable.product_kangkung
            "tukubumbu" -> R.drawable.product_cabai
            "tukubenih" -> R.drawable.ic_store
            else -> R.drawable.ic_store // default fallback
        }
    }
    
    // Untuk sample data, bisa langsung assign gambar spesifik
    fun getSampleImageResource(index: Int, category: String): Int {
        return when (category) {
            "TukuBuah" -> {
                when (index % 3) {
                    0 -> R.drawable.product_apel
                    1 -> R.drawable.product_jeruk
                    else -> R.drawable.product_apel
                }
            }
            "TukuSayur" -> {
                when (index % 2) {
                    0 -> R.drawable.product_kangkung
                    else -> R.drawable.product_kangkung
                }
            }
            "TukuBumbu" -> {
                R.drawable.product_cabai
            }
            "TukuBenih" -> {
                R.drawable.ic_store
            }
            else -> R.drawable.ic_store
        }
    }
}