package com.example.minigrocerydeliveryapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minigrocerydeliveryapp.data.PreferenceManager
import com.example.minigrocerydeliveryapp.databinding.DialogAddressEntryBinding
import com.example.minigrocerydeliveryapp.databinding.FragmentSavedAddressesBinding
import com.example.minigrocerydeliveryapp.model.UserAddress
import com.example.minigrocerydeliveryapp.ui.adapters.AddressAdapter
import com.example.minigrocerydeliveryapp.viewmodel.GroceryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SavedAddressesFragment : Fragment() {

    private var _binding: FragmentSavedAddressesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroceryViewModel by activityViewModels()
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedAddressesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        addressAdapter = AddressAdapter { address ->
            viewModel.removeAddress(address)
        }
        binding.rvAddresses.apply {
            adapter = addressAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnAddAddress.setOnClickListener {
            showAddressDialog()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addressList.collectLatest { addresses ->
                if (addresses.isEmpty()) {
                    binding.rvAddresses.visibility = View.GONE
                    binding.tvNoAddresses.visibility = View.VISIBLE
                } else {
                    binding.rvAddresses.visibility = View.VISIBLE
                    binding.tvNoAddresses.visibility = View.GONE
                    addressAdapter.submitList(addresses)
                }
            }
        }
    }

    private fun showAddressDialog() {
        val dialogBinding = DialogAddressEntryBinding.inflate(layoutInflater)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnSave.setOnClickListener {
            val name = dialogBinding.tilName.editText?.text.toString()
            val phone = dialogBinding.tilPhone.editText?.text.toString()
            val flat = dialogBinding.tilFlat.editText?.text.toString()
            val city = dialogBinding.tilCity.editText?.text.toString()
            val pincode = dialogBinding.tilPincode.editText?.text.toString()

            var isValid = true
            if (name.isBlank()) {
                dialogBinding.tilName.error = "Name required"
                isValid = false
            }
            if (phone.length != 10) {
                dialogBinding.tilPhone.error = "10-digit number required"
                isValid = false
            }
            if (flat.isBlank()) {
                dialogBinding.tilFlat.error = "Address required"
                isValid = false
            }
            if (city.isBlank()) {
                dialogBinding.tilCity.error = "City required"
                isValid = false
            }
            if (pincode.length != 6) {
                dialogBinding.tilPincode.error = "6-digit pincode required"
                isValid = false
            }

            if (isValid) {
                val newAddress = UserAddress(
                    name = name,
                    phone = phone,
                    flatHouseNo = flat,
                    areaStreet = dialogBinding.tilArea.editText?.text.toString(),
                    landmark = dialogBinding.tilLandmark.editText?.text.toString(),
                    city = city,
                    pincode = pincode
                )
                viewModel.addAddress(newAddress)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
