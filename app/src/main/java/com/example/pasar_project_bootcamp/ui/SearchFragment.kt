package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.databinding.FragmentSearchBinding
import com.example.pasar_project_bootcamp.ui.adapter.RecommendationAdapter
import com.example.pasar_project_bootcamp.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendationAdapter: RecommendationAdapter
    private val viewModel: SearchViewModel by viewModels()

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
        observeRecommendations()
        viewModel.loadRecommendations()
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
    }

    private fun setupRecommendations() {
        recommendationAdapter = RecommendationAdapter(emptyList()) { recommendation ->
            binding.searchEditText.setText(recommendation)
            performSearch()
        }

        binding.recommendationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recommendationAdapter
        }
    }

    private fun observeRecommendations() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recommendations.collect { products ->
                    recommendationAdapter.updateData(products.map { it.name })
                }
            }
        }
    }

    private fun performSearch() {
        val query = binding.searchEditText.text.toString().trim()
        if (query.isNotEmpty()) {
            val bundle = bundleOf("searchQuery" to query, "category" to "")
            findNavController().navigate(R.id.action_searchFragment_to_productListFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
