package com.example.pasar_project_bootcamp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasar_project_bootcamp.adapter.CategoryAdapter
import com.example.pasar_project_bootcamp.adapter.ProductAdapter
import com.example.pasar_project_bootcamp.data.Category
import com.example.pasar_project_bootcamp.data.Product

class MainActivity : AppCompatActivity() {
    
    private lateinit var searchEditText: EditText
    private lateinit var cartIcon: ImageView
    private lateinit var greetingText: TextView
    private lateinit var balanceText: TextView
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var productsRecyclerView: RecyclerView
    
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        setupRecyclerViews()
        loadData()
    }
    
    private fun initViews() {
        searchEditText = findViewById(R.id.et_search)
        cartIcon = findViewById(R.id.iv_cart)
        greetingText = findViewById(R.id.tv_greeting)
        balanceText = findViewById(R.id.tv_balance)
        categoriesRecyclerView = findViewById(R.id.rv_categories)
        productsRecyclerView = findViewById(R.id.rv_products)
        
        // Set greeting
        greetingText.text = "Hai, John Doe!"
        balanceText.text = "Rp 150.000,00"
        
        // Setup search click listener
        searchEditText.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        
        cartIcon.setOnClickListener {
            // Handle cart click
        }
    }
    
    private fun setupRecyclerViews() {
        // Categories RecyclerView
        categoriesRecyclerView.layoutManager = GridLayoutManager(this, 4)
        categoryAdapter = CategoryAdapter(getCategories()) { category ->
            // Handle category click
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("category", category.name)
            startActivity(intent)
        }
        categoriesRecyclerView.adapter = categoryAdapter
        
        // Products RecyclerView
        productsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductAdapter(getProducts()) { product ->
            // Handle product click
        }
        productsRecyclerView.adapter = productAdapter
    }
    
    private fun loadData() {
        // In a real app, this would load from Firebase
        // For now, we use mock data
    }
    
    private fun getCategories(): List<Category> {
        return listOf(
            Category("1", "TukuBuah", R.drawable.ic_fruit, R.color.green_light),
            Category("2", "TukuSayur", R.drawable.ic_vegetable, R.color.green_light),
            Category("3", "TukuBumbu", R.drawable.ic_spice, R.color.green_light),
            Category("4", "TukuBenih", R.drawable.ic_seed, R.color.green_light)
        )
    }
    
    private fun getProducts(): List<Product> {
        return listOf(
            Product("1", "Buah buah", 150000.0, "Buah", "Buah segar dari petani lokal", "", "farmer1", "Petani A", "Jakarta", 10),
            Product("2", "Sayur sayur", 150000.0, "Sayur", "Sayur organik segar", "", "farmer2", "Petani B", "Bandung", 15),
            Product("3", "Bumbu bumbu", 150000.0, "Bumbu", "Bumbu dapur alami", "", "farmer3", "Petani C", "Surabaya", 20),
            Product("4", "Benih benih", 150000.0, "Benih", "Benih berkualitas tinggi", "", "farmer4", "Petani D", "Medan", 5)
        )
    }
}