package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.databinding.FragmentSearchBinding
import com.example.pasar_project_bootcamp.ui.adapter.RecommendationAdapter

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendationAdapter: RecommendationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupRecommendations()
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
    }

    private fun setupRecommendations() {
        val recommendations = listOf(
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu",
            "Buah buah satu"
        )

        recommendationAdapter = RecommendationAdapter(recommendations) { recommendation ->
            // Handle recommendation click
            binding.searchEditText.setText(recommendation)
            performSearch()
        }

        binding.recommendationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recommendationAdapter
        }
    }

    private fun performSearch() {
        val query = binding.searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            val bundle = Bundle().apply {
                putString("category", query)
            }
            findNavController().navigate(R.id.action_searchFragment_to_productListFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}