package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.databinding.FragmentHomeBinding
import com.example.pasar_project_bootcamp.ui.adapter.ProductAdapter
import com.example.pasar_project_bootcamp.ui.viewmodel.HomeViewModel
import com.example.pasar_project_bootcamp.utils.BottomNavTab
import com.example.pasar_project_bootcamp.utils.setActiveTab
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGreeting()
        setupProductList()
        setupClickListeners()
        setupBottomNav()
        observeProducts()
    }

    private fun setupGreeting() {
        val email = FirebaseAuth.getInstance().currentUser?.email
        val name = email?.substringBefore("@") ?: "Pengguna"
        binding.welcomeText.text = "Hai, $name!"
    }

    private fun setupProductList() {
        productAdapter = ProductAdapter(
            onProductClick = { product ->
                val bundle = Bundle().apply {
                    putString("productId", product.id)
                }
                findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
            }
        )
        binding.homeProductRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = productAdapter
        }
    }

    private fun observeProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collect { products ->
                    productAdapter.submitList(products)
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.searchBar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.cartIcon.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }

        binding.categoryBuah.setOnClickListener {
            navigateToProductList("TukuBuah")
        }

        binding.categorySayur.setOnClickListener {
            navigateToProductList("TukuSayur")
        }

        binding.categoryBumbu.setOnClickListener {
            navigateToProductList("TukuBumbu")
        }

        binding.categoryBenih.setOnClickListener {
            navigateToProductList("TukuBenih")
        }
    }

    private fun setupBottomNav() {
        binding.bottomNavigation.setActiveTab(BottomNavTab.HOME)
        binding.bottomNavigation.navCart.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }
        binding.bottomNavigation.navProfile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun navigateToProductList(category: String) {
        val bundle = Bundle().apply {
            putString("category", category)
        }
        findNavController().navigate(R.id.action_homeFragment_to_productListFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
