package com.example.pasar_project_bootcamp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasar_project_bootcamp.adapter.ProductAdapter
import com.example.pasar_project_bootcamp.data.Product

class SearchActivity : AppCompatActivity() {
    
    private lateinit var backButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var filterButton: ImageView
    private lateinit var searchPromptLayout: LinearLayout
    private lateinit var searchPromptText: TextView
    private lateinit var recommendationLayout: LinearLayout
    private lateinit var recommendationRecyclerView: RecyclerView
    private lateinit var searchResultsRecyclerView: RecyclerView
    
    private lateinit var productAdapter: ProductAdapter
    private var allProducts: List<Product> = emptyList()
    private var isSearchMode = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        
        initViews()
        setupSearch()
        loadData()
        
        // Check if opened from category
        val category = intent.getStringExtra("category")
        if (!category.isNullOrEmpty()) {
            searchEditText.setText(category)
            performSearch(category)
        }
    }
    
    private fun initViews() {
        backButton = findViewById(R.id.iv_back)
        searchEditText = findViewById(R.id.et_search)
        filterButton = findViewById(R.id.iv_filter)
        searchPromptLayout = findViewById(R.id.ll_search_prompt)
        searchPromptText = findViewById(R.id.tv_search_prompt)
        recommendationLayout = findViewById(R.id.ll_recommendation)
        recommendationRecyclerView = findViewById(R.id.rv_recommendation)
        searchResultsRecyclerView = findViewById(R.id.rv_search_results)
        
        backButton.setOnClickListener {
            finish()
        }
        
        filterButton.setOnClickListener {
            // Handle filter click
        }
    }
    
    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    performSearch(query)
                } else {
                    showSearchPrompt()
                }
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
        
        // Initially show search prompt
        showSearchPrompt()
    }
    
    private fun showSearchPrompt() {
        isSearchMode = false
        searchPromptLayout.visibility = View.VISIBLE
        recommendationLayout.visibility = View.VISIBLE
        searchResultsRecyclerView.visibility = View.GONE
        
        searchPromptText.text = "Apakah ini yang kamu cari:"
        
        // Setup recommendation RecyclerView
        recommendationRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val recommendationAdapter = RecommendationAdapter(getRecommendations()) { recommendation ->
            searchEditText.setText(recommendation)
            performSearch(recommendation)
        }
        recommendationRecyclerView.adapter = recommendationAdapter
    }
    
    private fun performSearch(query: String) {
        isSearchMode = true
        searchPromptLayout.visibility = View.GONE
        recommendationLayout.visibility = View.GONE
        searchResultsRecyclerView.visibility = View.VISIBLE
        
        // Filter products based on query
        val filteredProducts = allProducts.filter { product ->
            product.name.contains(query, ignoreCase = true) ||
            product.category.contains(query, ignoreCase = true) ||
            product.description.contains(query, ignoreCase = true)
        }
        
        // Setup search results RecyclerView
        searchResultsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductAdapter(filteredProducts) { product ->
            // Handle product click
        }
        searchResultsRecyclerView.adapter = productAdapter
    }
    
    private fun loadData() {
        allProducts = getAllProducts()
    }
    
    private fun getRecommendations(): List<String> {
        return listOf(
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu"
        )
    }
    
    private fun getAllProducts(): List<Product> {
        return listOf(
            Product("1", "Buah buah", 150000.0, "Buah", "Buah segar dari petani lokal", "", "farmer1", "Petani A", "Jakarta", 10),
            Product("2", "Sayur sayur", 150000.0, "Sayur", "Sayur organik segar", "", "farmer2", "Petani B", "Bandung", 15),
            Product("3", "Bumbu bumbu", 150000.0, "Bumbu", "Bumbu dapur alami", "", "farmer3", "Petani C", "Surabaya", 20),
            Product("4", "Benih benih", 150000.0, "Benih", "Benih berkualitas tinggi", "", "farmer4", "Petani D", "Medan", 5),
            Product("5", "Buah buah", 150000.0, "Buah", "Buah segar dari petani lokal", "", "farmer5", "Petani E", "Yogyakarta", 8),
            Product("6", "Sayur sayur", 150000.0, "Sayur", "Sayur organik segar", "", "farmer6", "Petani F", "Semarang", 12)
        )
    }
}