package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.databinding.FragmentProductListBinding
import com.example.pasar_project_bootcamp.ui.adapter.ProductAdapter
import com.example.pasar_project_bootcamp.ui.viewmodel.ProductListViewModel
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private val viewModel: ProductListViewModel by viewModels()
    private var category: String = ""
    private var searchQuery: String = ""
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

        category = arguments?.getString("category") ?: ""
        searchQuery = arguments?.getString("searchQuery") ?: ""

        setupViews()
        setupProductList()
        observeProducts()
        viewModel.load(category, searchQuery)
    }

    private fun setupViews() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.filterButton.setOnClickListener {
            Toast.makeText(context, "Filter feature coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.searchEditText.setOnEditorActionListener { _, _, _ ->
            performSearch()
            true
        }

        binding.searchEditText.hint = when {
            searchQuery.isNotEmpty() -> "Cari \"$searchQuery\"..."
            category.isNotEmpty() -> "Cari $category..."
            else -> "Cari produk..."
        }
    }

    private fun setupProductList() {
        productAdapter = ProductAdapter(
            onProductClick = { product ->
                val bundle = Bundle().apply {
                    putString("productId", product.id)
                }
                findNavController().navigate(R.id.action_productListFragment_to_productDetailFragment, bundle)
            }
        )

        binding.productRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }

    private fun observeProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collect { products ->
                    allProducts = products
                    productAdapter.submitList(products)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isEmpty.collect { isEmpty ->
                    if (isEmpty) {
                        val message = when {
                            searchQuery.isNotEmpty() -> "Tidak ada produk yang sesuai dengan \"$searchQuery\""
                            category.isNotEmpty() -> "Tidak ada produk dalam kategori $category"
                            else -> "Tidak ada produk tersedia"
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun performSearch() {
        val query = binding.searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
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
            productAdapter.submitList(allProducts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
