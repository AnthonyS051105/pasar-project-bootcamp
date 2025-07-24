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

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private lateinit var firebaseHelper: FirebaseHelper
    private var category: String = ""
    private var allProducts: List<Product> = emptyList()

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

        firebaseHelper = FirebaseHelper()

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
        // You could add a progress bar here

        if (category.isNotEmpty()) {
            // Load products by category from Firebase
            firebaseHelper.getProductsByCategory(category) { products ->
                allProducts = products
                productAdapter.submitList(products)

                if (products.isEmpty()) {
                    Toast.makeText(context, "Tidak ada produk dalam kategori $category", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Load all products from Firebase
            firebaseHelper.getProducts { products ->
                allProducts = products
                productAdapter.submitList(products)

                if (products.isEmpty()) {
                    Toast.makeText(context, "Tidak ada produk tersedia", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun performSearch() {
        val query = binding.searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            // Filter products based on search query
            val filteredProducts = allProducts.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true) ||
                        product.category.contains(query, ignoreCase = true) ||
                        product.farmerName.contains(query, ignoreCase = true)
            }
            productAdapter.submitList(filteredProducts)

            if (filteredProducts.isEmpty()) {
                Toast.makeText(context, "Tidak ada produk yang sesuai dengan pencarian", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Show all products if search is empty
            productAdapter.submitList(allProducts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}