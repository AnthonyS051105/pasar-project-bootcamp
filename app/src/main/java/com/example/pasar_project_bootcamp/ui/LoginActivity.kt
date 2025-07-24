package com.example.pasar_project_bootcamp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pasar_project_bootcamp.MainActivity
import com.example.pasar_project_bootcamp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var firebaseAuth : FirebaseAuth

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        if (firebaseAuth.currentUser != null) {
            Log.d(TAG, "User already logged in, navigating to MainActivity")
            navigateToMain()
            return
        }

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            if (validateInput(email, password)) {
                performLogin(email, password)
            }
        }

        binding.signupText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        when {
            email.isEmpty() -> {
                binding.loginEmail.error = "Email tidak boleh kosong"
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.loginEmail.error = "Format email tidak valid"
                return false
            }
            password.isEmpty() -> {
                binding.loginPassword.error = "Password tidak boleh kosong"
                return false
            }
            password.length < 6 -> {
                binding.loginPassword.error = "Password minimal 6 karakter"
                return false
            }
        }
        return true
    }

    private fun performLogin(email: String, password: String) {
        Log.d(TAG, "Attempting login for email: $email")
        
        binding.loginButton.isEnabled = false
        binding.loginButton.text = "Logging in..."

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.loginButton.isEnabled = true
                binding.loginButton.text = "Login"
                
                if (task.isSuccessful) {
                    Log.d(TAG, "Login successful for user: ${firebaseAuth.currentUser?.uid}")
                    Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    val exception = task.exception
                    Log.e(TAG, "Login failed", exception)
                    
                    val errorMessage = when (exception) {
                        is FirebaseAuthException -> {
                            when (exception.errorCode) {
                                "ERROR_INVALID_EMAIL" -> "Format email tidak valid"
                                "ERROR_WRONG_PASSWORD" -> "Password salah"
                                "ERROR_USER_NOT_FOUND" -> "Email tidak terdaftar"
                                "ERROR_USER_DISABLED" -> "Akun telah dinonaktifkan"
                                "ERROR_TOO_MANY_REQUESTS" -> "Terlalu banyak percobaan. Coba lagi nanti"
                                "ERROR_NETWORK_REQUEST_FAILED" -> "Periksa koneksi internet Anda"
                                else -> "Login gagal: ${exception.message}"
                            }
                        }
                        else -> "Login gagal. Periksa email dan password Anda"
                    }
                    
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}