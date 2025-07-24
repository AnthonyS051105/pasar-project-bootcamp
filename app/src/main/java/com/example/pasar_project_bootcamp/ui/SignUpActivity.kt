package com.example.pasar_project_bootcamp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pasar_project_bootcamp.databinding.ActivitySignupBinding
import com.example.pasar_project_bootcamp.utils.FirebaseConfigChecker
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

        initializeFirebase()
        setupClickListeners()
    }

    private fun initializeFirebase() {
        try {
            // Check Firebase configuration first
            val configValid = FirebaseConfigChecker.checkFirebaseConfiguration(this)
            if (!configValid) {
                Log.w(TAG, "Firebase configuration check failed")
            }

            // Log Firebase info
            Log.d(TAG, FirebaseConfigChecker.getFirebaseInfo())

            firebaseAuth = FirebaseAuth.getInstance()
            firestore = FirebaseFirestore.getInstance()
            Log.d(TAG, "Firebase initialized successfully")

            // Test Firebase connection
            firebaseAuth.addAuthStateListener { auth ->
                Log.d(TAG, "Auth state changed. User: ${auth.currentUser?.email ?: "null"}")
            }

        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization failed", e)
            Toast.makeText(this, "Firebase initialization error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupClickListeners() {
        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString().trim()
            val password = binding.signupPassword.text.toString().trim()

            Log.d(TAG, "Signup button clicked. Email: $email, Password length: ${password.length}")

            if (validateInput(email, password)) {
                performSignUp(email, password)
            }
        }

        binding.loginText.setOnClickListener {
            Log.d(TAG, "Navigating to login")
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        Log.d(TAG, "Validating input - Email: $email, Password length: ${password.length}")

        when {
            email.isEmpty() -> {
                binding.signupEmail.error = "Email tidak boleh kosong"
                Log.w(TAG, "Validation failed: Empty email")
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.signupEmail.error = "Format email tidak valid"
                Log.w(TAG, "Validation failed: Invalid email format")
                return false
            }
            password.isEmpty() -> {
                binding.signupPassword.error = "Password tidak boleh kosong"
                Log.w(TAG, "Validation failed: Empty password")
                return false
            }
            password.length < 6 -> {
                binding.signupPassword.error = "Password minimal 6 karakter"
                Log.w(TAG, "Validation failed: Password too short")
                return false
            }
        }
        Log.d(TAG, "Input validation passed")
        return true
    }

    private fun performSignUp(email: String, password: String) {
        Log.d(TAG, "Starting signup process for email: $email")

        // Check Firebase Auth instance
        if (!::firebaseAuth.isInitialized) {
            Log.e(TAG, "Firebase Auth not initialized")
            Toast.makeText(this, "Firebase Auth tidak terinisialisasi", Toast.LENGTH_LONG).show()
            return
        }

        binding.signupButton.isEnabled = false
        binding.signupButton.text = "Creating Account..."

        Log.d(TAG, "Calling Firebase createUserWithEmailAndPassword")

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                Log.d(TAG, "Firebase createUser task completed. Success: ${task.isSuccessful}")

                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Log.d(TAG, "Signup successful. User ID: ${user?.uid}, Email: ${user?.email}")

                    // Create user profile in Firestore
                    createUserProfile(email)
                } else {
                    handleSignupError(task.exception)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Firebase createUser failed with exception", exception)
                handleSignupError(exception)
            }
    }

    private fun handleSignupError(exception: Exception?) {
        binding.signupButton.isEnabled = true
        binding.signupButton.text = "Sign Up"

        Log.e(TAG, "Signup failed with exception: ${exception?.javaClass?.simpleName}", exception)

        val errorMessage = when (exception) {
            is FirebaseAuthException -> {
                Log.e(TAG, "FirebaseAuthException - Error code: ${exception.errorCode}")
                when (exception.errorCode) {
                    "ERROR_INVALID_EMAIL" -> "Format email tidak valid"
                    "ERROR_WEAK_PASSWORD" -> "Password terlalu lemah (minimal 6 karakter)"
                    "ERROR_EMAIL_ALREADY_IN_USE" -> "Email sudah terdaftar. Silakan login atau gunakan email lain"
                    "ERROR_NETWORK_REQUEST_FAILED" -> "Gagal terhubung ke server. Periksa koneksi internet Anda"
                    "ERROR_TOO_MANY_REQUESTS" -> "Terlalu banyak percobaan. Coba lagi nanti"
                    "ERROR_OPERATION_NOT_ALLOWED" -> "Signup dengan email/password tidak diaktifkan"
                    else -> "Firebase Auth Error (${exception.errorCode}): ${exception.message}"
                }
            }
            else -> {
                val message = exception?.message ?: "Unknown error"
                Log.e(TAG, "Non-Firebase exception: $message")
                "Signup gagal: $message"
            }
        }

        Log.e(TAG, "Showing error message: $errorMessage")
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun createUserProfile(email: String) {
        val userId = firebaseAuth.currentUser?.uid
        Log.d(TAG, "Creating user profile for user ID: $userId")

        if (userId == null) {
            Log.e(TAG, "User ID is null, cannot create profile")
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_LONG).show()
            return
        }

        val userProfile = hashMapOf(
            "email" to email,
            "createdAt" to System.currentTimeMillis(),
            "name" to email.substringBefore("@"),
            "role" to "customer"
        )

        Log.d(TAG, "Saving user profile to Firestore: $userProfile")

        firestore.collection("users")
            .document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                Log.d(TAG, "User profile created successfully in Firestore")
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
                Log.e(TAG, "Error creating user profile in Firestore", exception)
                binding.signupButton.isEnabled = true
                binding.signupButton.text = "Sign Up"

                Toast.makeText(this, "Akun dibuat tapi profil gagal disimpan: ${exception.message}", Toast.LENGTH_LONG).show()

                // Still navigate to login even if profile creation fails
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }
}