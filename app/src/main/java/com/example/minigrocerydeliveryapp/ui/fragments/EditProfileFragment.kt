package com.example.minigrocerydeliveryapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.minigrocerydeliveryapp.databinding.FragmentEditProfileBinding
import com.example.minigrocerydeliveryapp.model.UserProfile
import com.example.minigrocerydeliveryapp.viewmodel.GroceryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroceryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userProfile.collectLatest { profile ->
                binding.tilName.editText?.setText(profile.name)
                binding.tilEmail.editText?.setText(profile.email)
                binding.tilPhone.editText?.setText(profile.phone)
            }
        }
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            val updatedProfile = UserProfile(
                name = binding.tilName.editText?.text.toString(),
                email = binding.tilEmail.editText?.text.toString(),
                phone = binding.tilPhone.editText?.text.toString()
            )
            viewModel.updateProfile(updatedProfile)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
