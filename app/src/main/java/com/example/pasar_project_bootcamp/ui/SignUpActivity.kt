package com.example.pasar_project_bootcamp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pasar_project_bootcamp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    companion object {
        private const val TAG = "SignUpActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString().trim()
            val password = binding.signupPassword.text.toString().trim()

            if (validateInput(email, password)) {
                performSignUp(email, password)
            }
        }

        binding.loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        when {
            email.isEmpty() -> {
                binding.signupEmail.error = "Email tidak boleh kosong"
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.signupEmail.error = "Format email tidak valid"
                return false
            }
            password.isEmpty() -> {
                binding.signupPassword.error = "Password tidak boleh kosong"
                return false
            }
            password.length < 6 -> {
                binding.signupPassword.error = "Password minimal 6 karakter"
                return false
            }
        }
        return true
    }

    private fun performSignUp(email: String, password: String) {
        Log.d(TAG, "Attempting signup for email: $email")
        
        binding.signupButton.isEnabled = false
        binding.signupButton.text = "Creating Account..."

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Signup successful for user: ${firebaseAuth.currentUser?.uid}")
                    
                    // Create user profile in Firestore
                    createUserProfile(email)
                } else {
                    binding.signupButton.isEnabled = true
                    binding.signupButton.text = "Sign Up"
                    
                    val exception = task.exception
                    Log.e(TAG, "Signup failed", exception)
                    
                    val errorMessage = when (exception) {
                        is FirebaseAuthException -> {
                            when (exception.errorCode) {
                                "ERROR_INVALID_EMAIL" -> "Format email tidak valid"
                                "ERROR_WEAK_PASSWORD" -> "Password terlalu lemah"
                                "ERROR_EMAIL_ALREADY_IN_USE" -> "Email sudah terdaftar"
                                "ERROR_NETWORK_REQUEST_FAILED" -> "Periksa koneksi internet Anda"
                                else -> "Signup gagal: ${exception.message}"
                            }
                        }
                        else -> "Signup gagal. Silakan coba lagi"
                    }
                    
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun createUserProfile(email: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        
        val userProfile = hashMapOf(
            "email" to email,
            "createdAt" to System.currentTimeMillis(),
            "name" to email.substringBefore("@"), // Default name from email
            "role" to "customer"
        )

        firestore.collection("users")
            .document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                Log.d(TAG, "User profile created successfully")
                binding.signupButton.isEnabled = true
                binding.signupButton.text = "Sign Up"
                
                Toast.makeText(this, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show()
                
                // Navigate to login
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error creating user profile", exception)
                binding.signupButton.isEnabled = true
                binding.signupButton.text = "Sign Up"
                
                Toast.makeText(this, "Akun dibuat tapi profil gagal disimpan", Toast.LENGTH_LONG).show()
                
                // Still navigate to login even if profile creation fails
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }
}