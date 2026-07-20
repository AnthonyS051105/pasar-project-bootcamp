package com.example.pasar_project_bootcamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pasar_project_bootcamp.data.UserProfile
import com.example.pasar_project_bootcamp.firebase.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(
    private val firebaseHelper: FirebaseHelper = FirebaseHelper()
) : ViewModel() {

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile.asStateFlow()

    val currentEmail: String?
        get() = FirebaseAuth.getInstance().currentUser?.email

    fun loadProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firebaseHelper.getUserProfile(uid) { result ->
            _profile.value = result
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}
