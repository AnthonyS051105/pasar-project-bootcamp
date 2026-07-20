package com.example.pasar_project_bootcamp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.pasar_project_bootcamp.R
import com.example.pasar_project_bootcamp.databinding.FragmentProfileBinding
import com.example.pasar_project_bootcamp.ui.viewmodel.ProfileViewModel
import com.example.pasar_project_bootcamp.utils.BottomNavTab
import com.example.pasar_project_bootcamp.utils.setActiveTab
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvProfileEmail.text = viewModel.currentEmail ?: "-"

        observeProfile()
        setupClickListeners()
        setupBottomNav()
        viewModel.loadProfile()
    }

    private fun observeProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profile.collect { profile ->
                    binding.tvProfileName.text = profile?.name
                        ?: viewModel.currentEmail?.substringBefore("@")
                        ?: "Pengguna"
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupBottomNav() {
        binding.bottomNavigation.setActiveTab(BottomNavTab.PROFILE)
        binding.bottomNavigation.navHome.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment,
                null,
                navOptions { popUpTo(R.id.homeFragment) { inclusive = false } }
            )
        }
        binding.bottomNavigation.navCart.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
