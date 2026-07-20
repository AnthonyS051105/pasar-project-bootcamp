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
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.databinding.FragmentCartBinding
import com.example.pasar_project_bootcamp.ui.adapter.CartAdapter
import com.example.pasar_project_bootcamp.ui.viewmodel.CartViewModel
import com.example.pasar_project_bootcamp.utils.BottomNavTab
import com.example.pasar_project_bootcamp.utils.setActiveTab
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        setupBottomNav()
        observeCart()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            cartItems = emptyList(),
            editable = true,
            onQuantityChange = { item, newQty -> cartViewModel.updateQuantity(item.id, newQty) },
            onRemoveItem = { item -> cartViewModel.removeItem(item.id) }
        )
        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.items.collect { items ->
                    cartAdapter.submitList(items)
                    binding.tvCartTotal.text = formatCurrency(cartViewModel.getTotal())

                    val isEmpty = items.isEmpty()
                    binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
                    binding.rvCartItems.visibility = if (isEmpty) View.GONE else View.VISIBLE
                    binding.checkoutSection.visibility = if (isEmpty) View.GONE else View.VISIBLE
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_orderDetailFragment)
        }
        binding.btnShopNow.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment,
                null,
                navOptions { popUpTo(R.id.homeFragment) { inclusive = false } }
            )
        }
    }

    private fun setupBottomNav() {
        binding.bottomNavigation.setActiveTab(BottomNavTab.CART)
        binding.bottomNavigation.navHome.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment,
                null,
                navOptions { popUpTo(R.id.homeFragment) { inclusive = false } }
            )
        }
        binding.bottomNavigation.navProfile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return format.format(amount).replace("IDR", "Rp")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
