package com.example.minigrocerydeliveryapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.minigrocerydeliveryapp.R
import com.example.minigrocerydeliveryapp.data.PreferenceManager
import com.example.minigrocerydeliveryapp.databinding.DialogAddressEntryBinding
import com.example.minigrocerydeliveryapp.databinding.FragmentHomeBinding
import com.example.minigrocerydeliveryapp.model.UserAddress
import com.example.minigrocerydeliveryapp.ui.adapters.ProductAdapter
import com.example.minigrocerydeliveryapp.viewmodel.GroceryViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroceryViewModel by activityViewModels()
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        
        // Load saved address into ViewModel
        viewModel.updateAddress(preferenceManager.getAddress())

        setupRecyclerView()
        setupCategories()
        setupListeners()
        observeViewModel()
        setupBackNavigation()
    }

    private fun setupBackNavigation() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                
                // 1. If keyboard/search focus is active, clear it first
                if (binding.etSearch.hasFocus()) {
                    binding.etSearch.clearFocus()
                    imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                    return
                }

                // 2. If search text is not empty, clear the text
                if (binding.etSearch.text.isNotEmpty()) {
                    binding.etSearch.setText("")
                    return
                }

                // 3. Otherwise, perform default back action (exit app or go back)
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onProductClick = { product ->
                val bundle = Bundle().apply { putInt("productId", product.id) }
                findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
            },
            onAddToCart = { product ->
                viewModel.addToCart(product)
            }
        )
        binding.rvProducts.layoutManager = GridLayoutManager(context, 2)
        binding.rvProducts.adapter = productAdapter
    }

    private fun setupCategories() {
        viewModel.categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                id = View.generateViewId()
                setOnClickListener { viewModel.selectCategory(category) }
            }
            binding.chipGroupCategories.addView(chip)
            if (category == "All") chip.isChecked = true
        }
    }

    private fun setupListeners() {
        binding.tvAddress.setOnClickListener { showAddressDialog() }
        binding.fabCart.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_cartFragment) }
        binding.btnNotifications.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_notificationsFragment)
        }
        
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest { products ->
                productAdapter.submitList(products)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItems.collectLatest { cartItems ->
                val count = cartItems.sumOf { it.quantity }
                binding.fabCart.text = "Cart ($count)"
                binding.fabCart.visibility = if (count > 0) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userAddress.collectLatest { address ->
                binding.tvAddress.text = address.toDisplayString()
            }
        }
    }

    private fun showAddressDialog() {
        val dialogBinding = DialogAddressEntryBinding.inflate(layoutInflater)
        val currentAddress = viewModel.userAddress.value
        
        dialogBinding.tilName.editText?.setText(currentAddress.name)
        dialogBinding.tilPhone.editText?.setText(currentAddress.phone)
        dialogBinding.tilFlat.editText?.setText(currentAddress.flatHouseNo)
        dialogBinding.tilArea.editText?.setText(currentAddress.areaStreet)
        dialogBinding.tilLandmark.editText?.setText(currentAddress.landmark)
        dialogBinding.tilCity.editText?.setText(currentAddress.city)
        dialogBinding.tilPincode.editText?.setText(currentAddress.pincode)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        // Add a way to close the dialog if needed (optional, but good practice since cancelable=false)
        // However, the screenshot shows a very clean UI, so maybe just make it cancelable via back button but not touch outside?
        // Actually, setCancelable(false) prevents both.
        // Let's use dialog.setCanceledOnTouchOutside(false) instead if we want to allow back button.
        dialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnSave.setOnClickListener {
            val newAddress = UserAddress(
                name = dialogBinding.tilName.editText?.text.toString(),
                phone = dialogBinding.tilPhone.editText?.text.toString(),
                flatHouseNo = dialogBinding.tilFlat.editText?.text.toString(),
                areaStreet = dialogBinding.tilArea.editText?.text.toString(),
                landmark = dialogBinding.tilLandmark.editText?.text.toString(),
                city = dialogBinding.tilCity.editText?.text.toString(),
                pincode = dialogBinding.tilPincode.editText?.text.toString()
            )
            viewModel.updateAddress(newAddress)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
