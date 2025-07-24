package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pasar_project_bootcamp.BuildConfig
import com.example.pasar_project_bootcamp.databinding.FragmentRecipeBinding
import com.example.pasar_project_bootcamp.ui.adapter.RecipeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientsAdapter: RecipeAdapter
    private lateinit var stepsAdapter: RecipeAdapter

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    companion object {
        private const val TAG = "RecipeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerViews()

        val ingredients = arguments?.getStringArrayList("ingredients") ?: emptyList()

        if (ingredients.isNotEmpty()) {
            showLoadingState()
            generateRecipeFromAI(ingredients)
        } else {
            showEmptyState()
        }
    }

    private fun setupUI() {
        binding.tvJudul.text = "Inspirasi Resep"
        binding.ivRecipeInspiration.setImageResource(android.R.color.darker_gray)

        // Back button
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerViews() {
        ingredientsAdapter = RecipeAdapter()
        stepsAdapter = RecipeAdapter()

        binding.rvIngredients.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ingredientsAdapter
        }

        binding.rvSteps.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stepsAdapter
        }
    }

    private fun showLoadingState() {
        binding.tvJudul.text = "Sedang membuat resep..."
        ingredientsAdapter.setData(listOf("⏳ Memproses bahan-bahan..."))
        stepsAdapter.setData(listOf("⏳ Menyiapkan langkah-langkah..."))
    }

    private fun showEmptyState() {
        binding.tvJudul.text = "Tidak ada bahan"
        ingredientsAdapter.setData(listOf("❌ Tidak ada bahan untuk membuat resep"))
        stepsAdapter.setData(listOf("📝 Tambahkan bahan ke keranjang terlebih dahulu"))
        Toast.makeText(context, "Tidak ada bahan untuk membuat resep", Toast.LENGTH_SHORT).show()
    }

    private fun generateRecipeFromAI(ingredients: List<String>) {
        val apiKey = BuildConfig.OPENROUTER_API_KEY
        val bahanText = ingredients.joinToString(", ")

        Log.d(TAG, "Generating recipe with ingredients: $bahanText")

        val prompt = """
            Misalkan kamu adalah seorang chef ternama
            Buatkan resep masakan sederhana menggunakan bahan: $bahanText
            
            Format:
            JUDUL: [nama resep]
            
            BAHAN:
            - [bahan 1 dengan takaran]
            - [bahan 2 dengan takaran]
            
            LANGKAH:
            1. [langkah 1]
            2. [langkah 2]
        """.trimIndent()

        val json = JSONObject().apply {
            put("model", "deepseek/deepseek-r1:free")
            put("prompt", prompt)
            put("max_tokens", 2000)
            put("temperature", 0.7)
        }


        val body = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://openrouter.ai/api/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "Network request failed", e)
                        CoroutineScope(Dispatchers.Main).launch {
                            showErrorState("Gagal terhubung ke server")
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        Log.d(TAG, "Response received: $responseBody")

                        CoroutineScope(Dispatchers.Main).launch {
                            if (response.isSuccessful && responseBody != null) {
                                try {
                                    val json = JSONObject(responseBody)

                                    // Ambil konten dari "text" (bukan lagi "message.content")
                                    val content = json
                                        .getJSONArray("choices")
                                        .getJSONObject(0)
                                        .getString("text")

                                    if (content.isNullOrBlank()) {
                                        Log.e(TAG, "Content kosong dari model")
                                        showErrorState("Model tidak mengembalikan resep.")
                                        return@launch
                                    }

                                    parseAndDisplayRecipe(content)
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error parsing response", e)
                                    showErrorState("Gagal memproses resep")
                                }
                            } else {
                                Log.e(TAG, "Unsuccessful response: ${response.code}, body: $responseBody")
                                showErrorState("Server bermasalah")
                            }


                        }
                    }
                })
            } catch (e: Exception) {
                Log.e(TAG, "Exception in generateRecipeFromAI", e)
                CoroutineScope(Dispatchers.Main).launch {
                    showErrorState("Terjadi kesalahan")
                }
            }
        }
    }

    private fun parseAndDisplayRecipe(content: String) {
        try {
            val lines = content.lines().map { it.trim() }.filter { it.isNotBlank() }

            var title = "Resep Inspirasi"
            val ingredientsList = mutableListOf<String>()
            val stepsList = mutableListOf<String>()

            var currentSection = ""

            for (line in lines) {
                when {
                    line.startsWith("JUDUL:", ignoreCase = true) -> {
                        title = line.substringAfter(":").trim()
                    }
                    line.equals("BAHAN:", ignoreCase = true) -> {
                        currentSection = "BAHAN"
                    }
                    line.equals("LANGKAH:", ignoreCase = true) -> {
                        currentSection = "LANGKAH"
                    }
                    line.startsWith("-") && currentSection == "BAHAN" -> {
                        ingredientsList.add(line)
                    }
                    line.matches(Regex("^\\d+\\..*")) && currentSection == "LANGKAH" -> {
                        stepsList.add(line)
                    }
                    line.isNotEmpty() && currentSection == "BAHAN" -> {
                        ingredientsList.add("- $line")
                    }
                    line.isNotEmpty() && currentSection == "LANGKAH" -> {
                        if (!stepsList.isEmpty() || line.contains("langkah", ignoreCase = true)) {
                            stepsList.add(line)
                        }
                    }
                }
            }

            // Fallback jika parsing gagal
            if (ingredientsList.isEmpty() || stepsList.isEmpty()) {
                parseSimple(content, ingredientsList, stepsList)
            }

            // Update UI
            binding.tvJudul.text = title

            ingredientsAdapter.setData(
                if (ingredientsList.isNotEmpty()) ingredientsList
                else listOf("- Data bahan tidak tersedia")
            )

            stepsAdapter.setData(
                if (stepsList.isNotEmpty()) stepsList
                else listOf("1. Data langkah tidak tersedia")
            )

            // Load image berdasarkan judul resep
            loadRecipeImage(title)

        } catch (e: Exception) {
            Log.e(TAG, "Error parsing recipe", e)
            showErrorState("Gagal menampilkan resep")
        }
    }

    private fun loadRecipeImage(recipeTitle: String) {
        Log.d(TAG, "Starting image load for recipe: $recipeTitle")

        // Langsung load gambar default dulu untuk memastikan ImageView berfungsi
        loadDefaultRecipeImage()

        // Cek apakah API key tersedia
        val unsplashApiKey = BuildConfig.UNSPLASH_ACCESS_KEY
        if (unsplashApiKey.isEmpty()) {
            Log.w(TAG, "Unsplash API key tidak tersedia, menggunakan gambar default")
            return
        }

        // Kemudian coba cari gambar dari Unsplash
        val searchQuery = "$recipeTitle food recipe Indonesian"
        Log.d(TAG, "Searching image for: $searchQuery")

        val encodedQuery = URLEncoder.encode(searchQuery, "UTF-8")
        val imageSearchUrl = "https://api.unsplash.com/search/photos?query=$encodedQuery&per_page=1&orientation=landscape"

        val request = Request.Builder()
            .url(imageSearchUrl)
            .addHeader("Authorization", "Client-ID $unsplashApiKey")
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                Log.d(TAG, "Unsplash API Response Code: ${response.code}")

                CoroutineScope(Dispatchers.Main).launch {
                    if (response.isSuccessful && responseBody != null) {
                        try {
                            val json = JSONObject(responseBody)
                            val results = json.getJSONArray("results")

                            if (results.length() > 0) {
                                val firstImage = results.getJSONObject(0)
                                val imageUrl = firstImage.getJSONObject("urls").getString("regular")

                                Log.d(TAG, "Found image URL from Unsplash: $imageUrl")
                                loadImageWithGlide(imageUrl)
                            } else {
                                Log.d(TAG, "No images found in Unsplash results")
                                // Default image sudah di-load sebelumnya
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing Unsplash response", e)
                            // Default image sudah di-load sebelumnya
                        }
                    } else {
                        Log.e(TAG, "Unsplash search unsuccessful: ${response.code}")
                        if (response.code == 401) {
                            Log.e(TAG, "Unsplash API key tidak valid atau expired")
                        }
                        // Default image sudah di-load sebelumnya
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in loadRecipeImage", e)
                // Default image sudah di-load sebelumnya
            }
        }
    }

    private fun loadImageWithGlide(imageUrl: String) {
        Log.d(TAG, "Loading image with Glide: $imageUrl")

        context?.let { ctx ->
            try {
                Glide.with(ctx)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.darker_gray)
                    .into(binding.ivRecipeInspiration)

                Log.d(TAG, "Glide load initiated successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading image with Glide", e)
                binding.ivRecipeInspiration.setImageResource(android.R.color.darker_gray)
            }
        }
    }

    private fun loadDefaultRecipeImage() {
        Log.d(TAG, "Loading default recipe image")

        // Gunakan array gambar default untuk resep
        val defaultImages = arrayOf(
            "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=800", // Food preparation
            "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800", // Food cooking
            "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=800", // Food ingredients
            "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=800"  // Cooking utensils
        )

        val randomImage = defaultImages.random()
        Log.d(TAG, "Selected default image: $randomImage")
        loadImageWithGlide(randomImage)
    }

    private fun parseSimple(content: String, ingredients: MutableList<String>, steps: MutableList<String>) {
        val lines = content.lines().filter { it.isNotBlank() }

        // Simple fallback parsing
        for (line in lines) {
            when {
                line.startsWith("-") -> ingredients.add(line)
                line.matches(Regex("^\\d+\\..*")) -> steps.add(line)
            }
        }
    }

    private fun showErrorState(message: String) {
        binding.tvJudul.text = "Gagal Membuat Resep"
        ingredientsAdapter.setData(listOf("❌ $message"))
        stepsAdapter.setData(listOf("🔄 Silakan coba lagi"))
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}