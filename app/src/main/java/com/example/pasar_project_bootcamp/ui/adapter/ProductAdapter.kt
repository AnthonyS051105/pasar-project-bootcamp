package com.example.pasar_project_bootcamp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

                // Load product image - handle different sources
                when {
                    product.imageUrl.startsWith("http") -> {
                        // Load from URL (Firebase Storage)
                        Glide.with(itemView.context)
                            .load(product.imageUrl)
                            .placeholder(ProductImageHelper.getProductImageResource(product.name, product.category))
                            .error(ProductImageHelper.getProductImageResource(product.name, product.category))
                            .into(productImage)
                    }
                    product.imageUrl.startsWith("drawable://") -> {
                        // Load from drawable reference
                        val drawableName = product.imageUrl.removePrefix("drawable://")
                        val imageResource = when (drawableName) {
                            "product_apel" -> R.drawable.product_apel
                            "product_jeruk" -> R.drawable.product_jeruk
                            "product_kangkung" -> R.drawable.product_kangkung
                            "product_cabai" -> R.drawable.product_cabai
                            else -> ProductImageHelper.getProductImageResource(product.name, product.category)
                        }
                        productImage.setImageResource(imageResource)
                    }
                    else -> {
                        // Use helper to determine image based on name/category
                        val imageResource = ProductImageHelper.getProductImageResource(product.name, product.category)
                        productImage.setImageResource(imageResource)
                    }
                }

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