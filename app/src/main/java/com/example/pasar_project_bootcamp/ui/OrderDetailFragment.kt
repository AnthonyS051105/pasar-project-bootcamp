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
import com.example.pasar_project_bootcamp.data.CartItem
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.databinding.FragmentOrderDetailBinding
import com.example.pasar_project_bootcamp.ui.adapter.CartAdapter
import java.text.NumberFormat
import java.util.Locale

class OrderDetailFragment : Fragment() {

    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()

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
        loadSampleData()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems)
        binding.rvOrderItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun loadSampleData() {
        // Sample cart items for demonstration
        val sampleProduct = Product(
            id = "1",
            name = "Buah buahan satu",
            price = 150000.0,
            imageUrl = "",
            farmerName = "Toko Beli Beli"
        )

        cartItems.add(
            CartItem(
                id = "1",
                product = sampleProduct,
                quantity = 1,
                totalPrice = sampleProduct.price
            )
        )

        cartAdapter.notifyDataSetChanged()
        updateTotalAmount()
    }

    private fun updateTotalAmount() {
        val total = cartItems.sumOf { it.totalPrice }
        binding.tvTotalAmount.text = formatCurrency(total)
    }

    // Implementasi Anda sendiri untuk mengambil ingredients dari cart
    private fun getIngredientsFromYourCart(): ArrayList<String> {
        // Contoh sederhana - Anda sesuaikan dengan struktur data Anda
        val ingredients = ArrayList<String>()

        // Ambil dari cartItems, orderItems, atau apapun struktur data Anda
        // Contoh:
        ingredients.add("ayam")
        ingredients.add("bawang merah")
        ingredients.add("tomat")
        ingredients.add("cabai")

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


    private fun setupClickListeners() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnRecipeInspiration.setOnClickListener {
                openRecipeInspiration()

            }

            btnPesan.setOnClickListener {
                // Navigate to payment screen
                findNavController().navigate(R.id.action_orderDetail_to_payment)
            }

            btnChangeAddress.setOnClickListener {
                // Show address selection dialog or navigate to address screen
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