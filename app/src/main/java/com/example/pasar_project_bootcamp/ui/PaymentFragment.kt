package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pasar_project_bootcamp.data.CartItem
import com.example.pasar_project_bootcamp.data.Order
import com.example.pasar_project_bootcamp.data.OrderStatus
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.databinding.FragmentPaymentBinding
import com.example.pasar_project_bootcamp.ui.adapter.CartAdapter
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadSampleData()
        setupClickListeners()
        setupPaymentMethodListener()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems)
        binding.rvPaymentItems.apply {
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
        binding.tvTotalPayment.text = formatCurrency(total)
    }

    private fun setupPaymentMethodListener() {
        binding.rgPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbCOD.id -> {
                    binding.llPaymentDetails.visibility = View.GONE
                }
                binding.rbBankTransfer.id, binding.rbEWallet.id -> {
                    binding.llPaymentDetails.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnBuatPesanan.setOnClickListener {
                processOrder()
            }
        }
    }

    private fun processOrder() {
        val deliveryAddress = binding.etDeliveryAddress.text.toString().trim()

        if (deliveryAddress.isEmpty()) {
            Toast.makeText(requireContext(), "Alamat pengiriman tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentMethod = when (binding.rgPaymentMethod.checkedRadioButtonId) {
            binding.rbCOD.id -> "COD"
            binding.rbBankTransfer.id -> "Bank Transfer"
            binding.rbEWallet.id -> "E-Wallet"
            else -> "COD"
        }

        // Validate payment details if not COD
        if (paymentMethod != "COD" && binding.llPaymentDetails.visibility == View.VISIBLE) {
            val paymentAccount = binding.etPaymentAccount.text.toString().trim()
            val accountName = binding.etAccountName.text.toString().trim()

            if (paymentAccount.isEmpty() || accountName.isEmpty()) {
                Toast.makeText(requireContext(), "Lengkapi detail pembayaran", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Create order
        val order = Order(
            id = generateOrderId(),
            userId = "user123", // Would come from Firebase Auth
            items = cartItems.toList(),
            totalAmount = cartItems.sumOf { it.totalPrice },
            deliveryAddress = deliveryAddress,
            orderDate = Date(),
            status = OrderStatus.PENDING,
            paymentMethod = paymentMethod
        )

        // Save order to Firebase (implementation would be here)
        saveOrderToFirebase(order)

        Toast.makeText(requireContext(), "Pesanan berhasil dibuat!", Toast.LENGTH_SHORT).show()

        // Navigate back to home or order confirmation
        findNavController().popBackStack()
    }

    private fun generateOrderId(): String {
        return "ORDER_${System.currentTimeMillis()}"
    }

    private fun saveOrderToFirebase(order: Order) {
        // Implementation would save order to Firebase Firestore
        // FirebaseFirestore.getInstance()
        //     .collection("orders")
        //     .document(order.id)
        //     .set(order)
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