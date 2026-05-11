package com.example.minigrocerydeliveryapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.minigrocerydeliveryapp.R
import com.example.minigrocerydeliveryapp.data.PreferenceManager
import com.example.minigrocerydeliveryapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    private var isOtpStep = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())

        binding.btnContinue.setOnClickListener {
            if (!isOtpStep) {
                val phone = binding.etPhone.text.toString()
                if (phone.length == 10) {
                    switchToOtpStep()
                } else {
                    binding.tilPhone.error = "Enter a valid 10-digit number"
                }
            } else {
                val otp = binding.etOtp.text.toString()
                if (otp == "1234") {
                    preferenceManager.setLoggedIn(true)
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    binding.tilOtp.error = "Invalid OTP. Use 1234"
                }
            }
        }
    }

    private fun switchToOtpStep() {
        isOtpStep = true
        binding.tilPhone.visibility = View.GONE
        binding.tilOtp.visibility = View.VISIBLE
        binding.tvOtpHint.visibility = View.VISIBLE
        binding.btnContinue.text = "Verify & Login"
        binding.tvSubtitle.text = "Enter the 4-digit code sent to your number"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
