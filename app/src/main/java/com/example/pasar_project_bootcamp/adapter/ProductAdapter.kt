package com.example.pasar_project_bootcamp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.data.Product
import java.text.NumberFormat
import java.util.*

class ProductAdapter(
    private var products: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.iv_product_image)
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        val favoriteButton: ImageView = itemView.findViewById(R.id.iv_favorite)
        val cartButton: ImageView = itemView.findViewById(R.id.iv_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        
        holder.productName.text = product.name
        
        // Format price to Indonesian Rupiah
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        holder.productPrice.text = formatter.format(product.price)
        
        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(holder.productImage)
        
        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
        
        holder.favoriteButton.setOnClickListener {
            // Handle favorite action
        }
        
        holder.cartButton.setOnClickListener {
            // Handle cart action
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}