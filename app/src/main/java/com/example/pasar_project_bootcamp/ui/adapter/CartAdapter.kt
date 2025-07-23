package com.example.pasar_project_bootcamp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.data.CartItem
import com.example.pasar_project_bootcamp.databinding.ItemCartBinding
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(private val cartItems: List<CartItem>) : 
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(private val binding: ItemCartBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            with(binding) {
                tvProductName.text = cartItem.product.name
                tvProductPrice.text = formatCurrency(cartItem.product.price)
                tvQuantity.text = cartItem.quantity.toString()
                tvTotalPrice.text = formatCurrency(cartItem.totalPrice)

                // Load product image (placeholder for now)
                ivProductImage.setBackgroundColor(
                    itemView.context.resources.getColor(R.color.gray_200, null)
                )

                // If you have image URL, use Glide:
                // Glide.with(itemView.context)
                //     .load(cartItem.product.imageUrl)
                //     .placeholder(R.color.gray_200)
                //     .into(ivProductImage)
            }
        }

        private fun formatCurrency(amount: Double): String {
            val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            return format.format(amount).replace("IDR", "Rp")
        }
    }
}