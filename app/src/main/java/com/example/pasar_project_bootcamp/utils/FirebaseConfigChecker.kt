package com.example.pasar_project_bootcamp.utils

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseConfigChecker {
    private const val TAG = "FirebaseConfigChecker"

    fun checkFirebaseConfiguration(context: Context): Boolean {
        return try {
            Log.d(TAG, "Checking Firebase configuration...")

            // Check if Firebase is initialized
            val app = FirebaseApp.getInstance()
            Log.d(TAG, "Firebase app name: ${app.name}")
            Log.d(TAG, "Firebase project ID: ${app.options.projectId}")
            Log.d(TAG, "Firebase app ID: ${app.options.applicationId}")

            // Check Auth
            val auth = FirebaseAuth.getInstance()
            Log.d(TAG, "Firebase Auth instance: ${auth.javaClass.simpleName}")

            // Check Firestore
            val firestore = FirebaseFirestore.getInstance()
            Log.d(TAG, "Firestore instance: ${firestore.javaClass.simpleName}")

            // Test basic connectivity
            testFirebaseConnectivity()

            Log.d(TAG, "Firebase configuration check passed")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Firebase configuration check failed", e)
            false
        }
    }

    private fun testFirebaseConnectivity() {
        Log.d(TAG, "Testing Firebase connectivity...")

        // Test Auth connection
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            Log.d(TAG, "Auth state listener triggered. Current user: ${auth.currentUser?.uid}")
        }

        // Test Firestore connection
        FirebaseFirestore.getInstance()
            .collection("test")
            .limit(1)
            .get()
            .addOnSuccessListener {
                Log.d(TAG, "Firestore connectivity test successful")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Firestore connectivity test failed", exception)
            }
    }

    fun getFirebaseInfo(): String {
        return try {
            val app = FirebaseApp.getInstance()
            """
            Firebase Configuration:
            - Project ID: ${app.options.projectId}
            - App ID: ${app.options.applicationId}
            - API Key: ${app.options.apiKey?.take(10)}...
            - Database URL: ${app.options.databaseUrl ?: "Not set"}
            """.trimIndent()
        } catch (e: Exception) {
            "Firebase not initialized: ${e.message}"
        }
    }
}