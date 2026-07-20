package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.databinding.FragmentOrderSuccessBinding
import java.text.NumberFormat
import java.util.Locale

class OrderSuccessFragment : Fragment() {

    private var _binding: FragmentOrderSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderId = arguments?.getString("orderId") ?: ""
        val totalAmount = arguments?.getFloat("totalAmount") ?: 0f

        binding.tvOrderId.text = "Order ID: $orderId"
        binding.tvOrderTotal.text = "Total: ${formatCurrency(totalAmount.toDouble())}"

        binding.btnBackToHome.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment,
                null,
                navOptions { popUpTo(R.id.homeFragment) { inclusive = false } }
            )
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
