package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Search bar click listener
        binding.searchBar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        // Cart icon click listener
        binding.cartIcon.setOnClickListener {
            // Navigate to cart (if implemented)
        }

        // Category click listeners
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