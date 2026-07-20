package com.example.pasar_project_bootcamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pasar_project_bootcamp.data.CartItem
import com.example.pasar_project_bootcamp.data.CartManager
import com.example.pasar_project_bootcamp.data.Order
import com.example.pasar_project_bootcamp.data.OrderStatus
import com.example.pasar_project_bootcamp.firebase.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

sealed class PaymentUiState {
    object Idle : PaymentUiState()
    object Loading : PaymentUiState()
    data class Success(val orderId: String, val totalAmount: Double) : PaymentUiState()
    data class Error(val message: String) : PaymentUiState()
}

class PaymentViewModel(
    private val firebaseHelper: FirebaseHelper = FirebaseHelper()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    val cartItems: StateFlow<List<CartItem>> = CartManager.items

    fun submitOrder(deliveryAddress: String, paymentMethod: String) {
        val items = CartManager.items.value
        if (items.isEmpty()) {
            _uiState.value = PaymentUiState.Error("Keranjang masih kosong")
            return
        }
        if (deliveryAddress.isBlank()) {
            _uiState.value = PaymentUiState.Error("Alamat pengiriman tidak boleh kosong")
            return
        }

        val orderId = "ORDER_${System.currentTimeMillis()}"
        val totalAmount = items.sumOf { it.totalPrice }

        val order = Order(
            id = orderId,
            userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
            items = items.toList(),
            totalAmount = totalAmount,
            deliveryAddress = deliveryAddress,
            orderDate = Date(),
            status = OrderStatus.PENDING,
            paymentMethod = paymentMethod
        )

        _uiState.value = PaymentUiState.Loading
        firebaseHelper.saveOrder(order) { success ->
            if (success) {
                CartManager.clear()
                _uiState.value = PaymentUiState.Success(order.id, totalAmount)
            } else {
                _uiState.value = PaymentUiState.Error("Gagal menyimpan pesanan. Silakan coba lagi.")
            }
        }
    }

    fun resetState() {
        _uiState.value = PaymentUiState.Idle
    }
}
