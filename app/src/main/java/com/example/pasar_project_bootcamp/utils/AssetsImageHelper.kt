package com.example.pasar_project_bootcamp.utils

import android.content.Context
import android.graphics.drawable.Drawable
import java.io.IOException
import java.io.InputStream

object AssetsImageHelper {

    fun loadImageFromAssets(context: Context, fileName: String): Drawable? {
        return try {
            val inputStream: InputStream = context.assets.open("images/$fileName")
            Drawable.createFromStream(inputStream, null)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getAssetImagePath(productName: String, category: String): String {
        val name = productName.lowercase()

        return when {
            name.contains("apel") -> "apel.jpg"
            name.contains("jeruk") -> "jeruk.jpg"
            name.contains("kangkung") -> "kangkung.jpg"
            name.contains("cabai") -> "cabai.jpg"
            else -> when (category.lowercase()) {
                "tukubuah" -> "default_fruit.jpg"
                "tukusayur" -> "default_vegetable.jpg"
                "tukubumbu" -> "default_spice.jpg"
                "tukubenih" -> "default_seed.jpg"
                else -> "default_product.jpg"
            }
        }
    }
}