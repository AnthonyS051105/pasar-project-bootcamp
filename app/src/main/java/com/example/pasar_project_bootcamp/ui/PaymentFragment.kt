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
import com.example.pasar_project_bootcamp.databinding.FragmentPaymentBinding
import com.example.pasar_project_bootcamp.ui.adapter.CartAdapter
import com.example.pasar_project_bootcamp.ui.viewmodel.PaymentUiState
import com.example.pasar_project_bootcamp.ui.viewmodel.PaymentViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

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
        observeCart()
        observeUiState()
        setupClickListeners()
        setupPaymentMethodListener()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems = emptyList(), editable = false)
        binding.rvPaymentItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartItems.collect { items ->
                    cartAdapter.submitList(items)
                    val total = items.sumOf { it.totalPrice }
                    binding.tvTotalPayment.text = formatCurrency(total)
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is PaymentUiState.Idle -> {
                            binding.btnBuatPesanan.isEnabled = true
                        }
                        is PaymentUiState.Loading -> {
                            binding.btnBuatPesanan.isEnabled = false
                        }
                        is PaymentUiState.Success -> {
                            binding.btnBuatPesanan.isEnabled = true
                            findNavController().navigate(
                                R.id.action_payment_to_orderSuccess,
                                bundleOf(
                                    "orderId" to state.orderId,
                                    "totalAmount" to state.totalAmount.toFloat()
                                )
                            )
                        }
                        is PaymentUiState.Error -> {
                            binding.btnBuatPesanan.isEnabled = true
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
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

        val paymentMethod = when (binding.rgPaymentMethod.checkedRadioButtonId) {
            binding.rbCOD.id -> "COD"
            binding.rbBankTransfer.id -> "Bank Transfer"
            binding.rbEWallet.id -> "E-Wallet"
            else -> "COD"
        }

        if (paymentMethod != "COD" && binding.llPaymentDetails.visibility == View.VISIBLE) {
            val paymentAccount = binding.etPaymentAccount.text.toString().trim()
            val accountName = binding.etAccountName.text.toString().trim()

            if (paymentAccount.isEmpty() || accountName.isEmpty()) {
                Toast.makeText(requireContext(), "Lengkapi detail pembayaran", Toast.LENGTH_SHORT).show()
                return
            }
        }

        viewModel.submitOrder(deliveryAddress, paymentMethod)
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
