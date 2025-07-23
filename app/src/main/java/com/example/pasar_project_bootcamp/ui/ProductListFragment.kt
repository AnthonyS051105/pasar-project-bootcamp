package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.databinding.FragmentProductListBinding
import com.example.pasar_project_bootcamp.ui.adapter.ProductAdapter
import com.example.pasar_project_bootcamp.firebase.FirebaseHelper
import com.example.pasar_project_bootcamp.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private var category: String = ""
    private val productRepository = ProductRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Get category from arguments
        category = arguments?.getString("category") ?: ""
        
        setupViews()
        setupProductList()
        loadProducts()
    }

    private fun setupViews() {
        // Back button click listener
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        // Filter button click listener
        binding.filterButton.setOnClickListener {
            Toast.makeText(context, "Filter feature coming soon", Toast.LENGTH_SHORT).show()
        }

        // Search functionality
        binding.searchEditText.setOnEditorActionListener { _, _, _ ->
            performSearch()
            true
        }

        // Set search hint based on category
        binding.searchEditText.hint = "Cari $category..."
    }

    private fun setupProductList() {
        productAdapter = ProductAdapter { product ->
            // Navigate to product detail
            val bundle = Bundle().apply {
                putString("productId", product.id)
            }
            findNavController().navigate(R.id.action_productListFragment_to_productDetailFragment, bundle)
        }

        binding.productRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }

    private fun loadProducts() {
        // Show loading state
        binding.productRecyclerView.alpha = 0.5f
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val products = if (category.isNotEmpty() && category.startsWith("Tuku")) {
                    // Load products by category from Repository (API + Firebase)
                    productRepository.getProductsByCategory(category)
                } else {
                    // Search query - search from Repository
                    productRepository.searchProducts(category)
                }
                
                binding.productRecyclerView.alpha = 1.0f
                if (products.isNotEmpty()) {
                    productAdapter.submitList(products)
                } else {
                    // Fallback to sample data if no products found
                    loadSampleProducts()
                }
            } catch (e: Exception) {
                // Handle error and fallback to sample data
                binding.productRecyclerView.alpha = 1.0f
                loadSampleProducts()
                Toast.makeText(context, "Error loading products: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadSampleProducts() {
        // Fallback sample products
        val products = when (category) {
            "TukuBuah" -> getSampleFruits()
            "TukuSayur" -> getSampleVegetables()
            "TukuBumbu" -> getSampleSpices()
            "TukuBenih" -> getSampleSeeds()
            else -> getAllSampleProducts()
        }
        productAdapter.submitList(products)
    }

    private fun performSearch() {
        val query = binding.searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            // Filter products based on search query
            val filteredProducts = getAllSampleProducts().filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                product.category.contains(query, ignoreCase = true)
            }
            productAdapter.submitList(filteredProducts)
        } else {
            loadProducts()
        }
    }

    private fun getSampleFruits(): List<Product> {
        return listOf(
            Product(
                id = "1",
                name = "Buah buah",
                price = 150000.0,
                imageUrl = "",
                category = "TukuBuah",
                farmerName = "Petani A",
                stock = 10
            ),
            Product(
                id = "2",
                name = "Buah buah",
                price = 150000.0,
                imageUrl = "",
                category = "TukuBuah",
                farmerName = "Petani B",
                stock = 15
            )
        )
    }

    private fun getSampleVegetables(): List<Product> {
        return listOf(
            Product(
                id = "3",
                name = "Sayur sayur",
                price = 150000.0,
                imageUrl = "",
                category = "TukuSayur",
                farmerName = "Petani C",
                stock = 20
            ),
            Product(
                id = "4",
                name = "Sayur sayur",
                price = 150000.0,
                imageUrl = "",
                category = "TukuSayur",
                farmerName = "Petani D",
                stock = 12
            )
        )
    }

    private fun getSampleSpices(): List<Product> {
        return listOf(
            Product(
                id = "5",
                name = "Bumbu bumbu",
                price = 150000.0,
                imageUrl = "",
                category = "TukuBumbu",
                farmerName = "Petani E",
                stock = 8
            ),
            Product(
                id = "6",
                name = "Bumbu bumbu",
                price = 150000.0,
                imageUrl = "",
                category = "TukuBumbu",
                farmerName = "Petani F",
                stock = 25
            )
        )
    }

    private fun getSampleSeeds(): List<Product> {
        return listOf(
            Product(
                id = "7",
                name = "Benih benih",
                price = 150000.0,
                imageUrl = "",
                category = "TukuBenih",
                farmerName = "Petani G",
                stock = 30
            ),
            Product(
                id = "8",
                name = "Benih benih",
                price = 150000.0,
                imageUrl = "",
                category = "TukuBenih",
                farmerName = "Petani H",
                stock = 18
            )
        )
    }

    private fun getAllSampleProducts(): List<Product> {
        return getSampleFruits() + getSampleVegetables() + getSampleSpices() + getSampleSeeds()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}