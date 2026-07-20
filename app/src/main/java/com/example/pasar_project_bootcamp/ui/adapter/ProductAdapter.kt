package com.example.pasar_project_bootcamp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.data.CartManager
import com.example.pasar_project_bootcamp.data.Product
import com.example.pasar_project_bootcamp.databinding.ItemProductBinding
import com.example.pasar_project_bootcamp.utils.ProductImageHelper
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onAddToCart: (Product) -> Unit = { product ->
        CartManager.addItem(product, 1)
    }
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

                val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                productPrice.text = formatter.format(product.price)

                val imageResource = when {
                    product.imageUrl.startsWith("drawable://") -> {
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
                        ProductImageHelper.getProductImageResource(product.name, product.category)
                    }
                    else -> {
                        ProductImageHelper.getProductImageResource(product.name, product.category)
                    }
                }

                productImage.setImageResource(imageResource)

                cardContainer.setOnClickListener {
                    onProductClick(product)
                }

                favoriteButton.setOnClickListener {
                    favoriteButton.isSelected = !favoriteButton.isSelected
                }

                cartButton.setOnClickListener {
                    onAddToCart(product)
                    Toast.makeText(
                        cardContainer.context,
                        "${product.name} ditambahkan ke keranjang",
                        Toast.LENGTH_SHORT
                    ).show()
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
