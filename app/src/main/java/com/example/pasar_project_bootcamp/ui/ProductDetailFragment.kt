package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.databinding.FragmentProductDetailBinding
import com.example.pasar_project_bootcamp.ui.viewmodel.ProductDetailViewModel
import com.example.pasar_project_bootcamp.utils.ProductImageHelper
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels()
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeState()

        val productId = arguments?.getString("productId") ?: ""
        if (productId.isEmpty()) {
            Toast.makeText(requireContext(), "Produk tidak ditemukan", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        viewModel.loadProduct(productId)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.product.collect { product ->
                    if (product != null) {
                        setupUI(product)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notFound.collect { notFound ->
                    if (notFound) {
                        Toast.makeText(requireContext(), "Produk tidak ditemukan", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun setupUI(product: Product) {
        with(binding) {
            val imageResource = ProductImageHelper.getProductImageResource(
                product.name,
                product.category
            )
            ivProductImage.setImageResource(imageResource)

            tvPrice.text = formatCurrency(product.price)
            tvProductName.text = product.name
            tvDescription.text = product.description
            tvStoreName.text = product.farmerName
            tvStoreAddress.text = product.farmerAddress
            tvRating.text = product.rating.toString()
            tvProductCount.text = product.stock.toString()
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            ivFavorite.setOnClickListener {
                isFavorite = !isFavorite
                ivFavorite.setColorFilter(
                    resources.getColor(
                        if (isFavorite) R.color.clay_error else R.color.gray_400,
                        null
                    )
                )
            }

            fabBuyNow.setOnClickListener {
                if (viewModel.product.value == null) {
                    Toast.makeText(requireContext(), "Produk belum siap", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.addToCart(1)
                findNavController().navigate(R.id.action_productDetail_to_orderDetail)
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
