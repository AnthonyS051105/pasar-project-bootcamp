package com.example.pasar_project_bootcamp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.databinding.FragmentProductDetailBinding
import com.example.pasar_project_bootcamp.utils.ProductImageHelper
import java.text.NumberFormat
import java.util.Locale

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var product: Product

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

        // Sample product for demonstration
        product = Product(
            id = "1",
            name = "Apel Malang",
            description = "Buah apel segar berkualitas tinggi langsung dari kebun petani lokal. Kaya akan vitamin dan mineral yang baik untuk kesehatan tubuh.",
            price = 150000.0,
            imageUrl = "drawable://product_apel",
            farmerId = "farmer1",
            farmerName = "Toko Beli Beli",
            farmerAddress = "Alamat Toko Beli Beli",
            category = "Buah-buahan",
            stock = 30,
            rating = 5.0f,
            totalReviews = 25
        )

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        with(binding) {
            // Load product image using ProductImageHelper
            val imageResource = ProductImageHelper.getProductImageResource(
                product.name,
                product.category
            )
            ivProductImage.setImageResource(imageResource)

            // Set product details
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
                // Toggle favorite status
                // Implementation would save to Firebase
            }

            fabBuyNow.setOnClickListener {
                // Navigate to order detail screen
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