package com.example.pasar_project_bootcamp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.databinding.ItemProductBinding
import com.example.pasar_project_bootcamp.utils.ProductImageHelper
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onProductClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                productName.text = product.name
                
                // Format price in Indonesian Rupiah
                val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                productPrice.text = formatter.format(product.price)

                // Load product image - simplified approach using only drawable resources
                val imageResource = when {
                    product.imageUrl.startsWith("drawable://") -> {
                        // Load from drawable reference
                        val drawableName = product.imageUrl.removePrefix("drawable://")
                        when (drawableName) {
                            "product_apel" -> R.drawable.product_apel
                            "product_jeruk" -> R.drawable.product_jeruk
                            "product_kangkung" -> R.drawable.product_kangkung
                            "product_cabai" -> R.drawable.product_cabai
                            else -> ProductImageHelper.getProductImageResource(product.name, product.category)
                        }
                    }
                    product.imageUrl.startsWith("http") -> {
                        // For HTTP URLs, use placeholder drawable for now
                        // In production, you would use an image loading library like Glide or Picasso
                        ProductImageHelper.getProductImageResource(product.name, product.category)
                    }
                    else -> {
                        // Use helper to determine image based on name/category
                        ProductImageHelper.getProductImageResource(product.name, product.category)
                    }
                }
                
                productImage.setImageResource(imageResource)

                // Set click listeners
                root.setOnClickListener {
                    onProductClick(product)
                }

                favoriteButton.setOnClickListener {
                    // Toggle favorite state
                    favoriteButton.isSelected = !favoriteButton.isSelected
                }

                cartButton.setOnClickListener {
                    // Add to cart functionality
                    onProductClick(product)
                }
            }
        }
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}