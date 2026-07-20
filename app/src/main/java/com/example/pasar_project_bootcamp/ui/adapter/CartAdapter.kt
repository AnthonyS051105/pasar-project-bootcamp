package com.example.pasar_project_bootcamp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pasar_project_bootcamp.data.CartItem
import com.example.pasar_project_bootcamp.databinding.ItemCartBinding
import com.example.pasar_project_bootcamp.utils.ProductImageHelper
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val editable: Boolean = true,
    private val onQuantityChange: (CartItem, Int) -> Unit = { _, _ -> },
    private val onRemoveItem: (CartItem) -> Unit = {}
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    fun submitList(items: List<CartItem>) {
        cartItems = items
        notifyDataSetChanged()
    }

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

                val imageResource = ProductImageHelper.getProductImageResource(
                    cartItem.product.name,
                    cartItem.product.category
                )
                ivProductImage.setImageResource(imageResource)

                if (editable) {
                    btnIncrease.visibility = android.view.View.VISIBLE
                    btnDecrease.visibility = android.view.View.VISIBLE
                    btnRemove.visibility = android.view.View.VISIBLE

                    btnIncrease.setOnClickListener {
                        onQuantityChange(cartItem, cartItem.quantity + 1)
                    }
                    btnDecrease.setOnClickListener {
                        onQuantityChange(cartItem, cartItem.quantity - 1)
                    }
                    btnRemove.setOnClickListener {
                        onRemoveItem(cartItem)
                    }
                } else {
                    btnIncrease.visibility = android.view.View.GONE
                    btnDecrease.visibility = android.view.View.GONE
                    btnRemove.visibility = android.view.View.GONE
                }
            }
        }

        private fun formatCurrency(amount: Double): String {
            val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            return format.format(amount).replace("IDR", "Rp")
        }
    }
}
