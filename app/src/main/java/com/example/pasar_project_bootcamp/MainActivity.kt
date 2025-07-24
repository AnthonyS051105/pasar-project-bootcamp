package com.example.pasar_project_bootcamp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.pasar_project_bootcamp.databinding.ActivityMainBinding
import com.example.pasar_project_bootcamp.firebase.FirebaseHelper
import com.example.pasar_project_bootcamp.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseHelper: FirebaseHelper
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseHelper = FirebaseHelper()

        // Check if user is logged in
        if (firebaseAuth.currentUser == null) {
            // User not logged in, redirect to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Initialize Firebase data
        firebaseHelper.initializeSampleData()

        // Hide action bar since we're using custom toolbar in fragments
        supportActionBar?.hide()
    }
}