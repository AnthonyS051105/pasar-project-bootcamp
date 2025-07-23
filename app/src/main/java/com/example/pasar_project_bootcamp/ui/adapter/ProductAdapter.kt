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

                // Load product image or use placeholder
                if (product.imageUrl.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(product.imageUrl)
                        .placeholder(R.drawable.ic_store)
                        .error(R.drawable.ic_store)
                        .into(productImage)
                } else {
                    productImage.setImageResource(R.drawable.ic_store)
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