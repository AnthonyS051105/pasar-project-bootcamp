package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

        fun newInstance(ingredients: ArrayList<String>): RecipeFragment {
            return RecipeFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList("ingredients", ingredients)
                }
            }
        }
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
        binding.ivRecipeInspiration.setBackgroundResource(android.R.color.darker_gray)

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
        val apiKey = "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"
        val bahanText = ingredients.joinToString(", ")

        Log.d(TAG, "Generating recipe with ingredients: $bahanText")

        val prompt = """
            Buatkan resep masakan Indonesia sederhana menggunakan bahan: $bahanText
            
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
            put("model", "deepseek-chat")
            put("messages", listOf(mapOf("role" to "user", "content" to prompt)))
            put("max_tokens", 800)
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
                                    val content = json
                                        .getJSONArray("choices")
                                        .getJSONObject(0)
                                        .getJSONObject("message")
                                        .getString("content")

                                    parseAndDisplayRecipe(content)
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error parsing response", e)
                                    showErrorState("Gagal memproses resep")
                                }
                            } else {
                                Log.e(TAG, "Unsuccessful response: ${response.code}")
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

        } catch (e: Exception) {
            Log.e(TAG, "Error parsing recipe", e)
            showErrorState("Gagal menampilkan resep")
        }
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