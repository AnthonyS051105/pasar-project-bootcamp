package com.example.pasar_project_bootcamp.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.databinding.FragmentOrderDetailBinding
import com.example.pasar_project_bootcamp.ui.adapter.CartAdapter
import com.example.pasar_project_bootcamp.ui.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class OrderDetailFragment : Fragment() {

    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter
    private var deliveryAddress: String = "Jl. Contoh Alamat No. 123, Jakarta"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeCart()
        binding.tvDeliveryAddress.text = deliveryAddress
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            cartItems = emptyList(),
            editable = true,
            onQuantityChange = { item, newQty -> cartViewModel.updateQuantity(item.id, newQty) },
            onRemoveItem = { item -> cartViewModel.removeItem(item.id) }
        )
        binding.rvOrderItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.items.collect { items ->
                    cartAdapter.submitList(items)
                    updateTotalAmount()
                }
            }
        }
    }

    private fun updateTotalAmount() {
        binding.tvTotalAmount.text = formatCurrency(cartViewModel.getTotal())
    }

    private fun getIngredientsFromYourCart(): ArrayList<String> {
        val ingredients = ArrayList<String>()
        for (item in cartViewModel.items.value) {
            val name = item.product.name.trim()
            if (name.isNotEmpty()) {
                ingredients.add(name)
            }
        }
        return ingredients
    }

    private fun openRecipeInspiration() {
        val ingredients = getIngredientsFromYourCart()

        if (ingredients.isEmpty()) {
            Toast.makeText(requireContext(), "Tidak ada bahan untuk membuat resep", Toast.LENGTH_SHORT).show()
            return
        }

        val bundle = Bundle().apply {
            putStringArrayList("ingredients", ingredients)
        }

        findNavController().navigate(R.id.action_orderDetail_to_recipe, bundle)
    }

    private fun showChangeAddressDialog() {
        val editText = EditText(requireContext()).apply {
            setText(deliveryAddress)
            setPadding(48, 32, 48, 32)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Ubah Alamat Pengiriman")
            .setView(editText)
            .setPositiveButton("Simpan") { _, _ ->
                val newAddress = editText.text.toString().trim()
                if (newAddress.isNotEmpty()) {
                    deliveryAddress = newAddress
                    binding.tvDeliveryAddress.text = deliveryAddress
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun setupClickListeners() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnRecipeInspiration.setOnClickListener {
                openRecipeInspiration()
            }

            btnPesan.setOnClickListener {
                if (cartViewModel.items.value.isEmpty()) {
                    Toast.makeText(requireContext(), "Keranjang masih kosong", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                findNavController().navigate(R.id.action_orderDetail_to_payment)
            }

            btnChangeAddress.setOnClickListener {
                showChangeAddressDialog()
            }
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
