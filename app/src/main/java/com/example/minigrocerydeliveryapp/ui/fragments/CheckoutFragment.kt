package com.example.minigrocerydeliveryapp.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.minigrocerydeliveryapp.R
import com.example.minigrocerydeliveryapp.data.PreferenceManager
import com.example.minigrocerydeliveryapp.databinding.DialogAddressEntryBinding
import com.example.minigrocerydeliveryapp.databinding.FragmentCheckoutBinding
import com.example.minigrocerydeliveryapp.model.UserAddress
import com.example.minigrocerydeliveryapp.viewmodel.GroceryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroceryViewModel by activityViewModels()
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.cardAddress.setOnClickListener {
            showAddressDialog()
        }
        binding.btnPlaceOrder.setOnClickListener {
            viewModel.placeOrder()
            findNavController().navigate(R.id.action_checkoutFragment_to_orderSuccessFragment)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userAddress.collectLatest { address ->
                if (address.isComplete()) {
                    binding.tvCheckoutName.text = address.name
                    binding.tvCheckoutAddress.text = address.toDisplayString()
                    binding.tvCheckoutPhone.text = address.phone
                } else {
                    binding.tvCheckoutName.text = "Set Location"
                    binding.tvCheckoutAddress.text = "Click here to add delivery address"
                    binding.tvCheckoutPhone.text = ""
                }
                
                binding.btnPlaceOrder.isEnabled = address.isComplete() && viewModel.cartItems.value.isNotEmpty()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItems.collectLatest { items ->
                binding.llOrderSummary.removeAllViews()
                
                // Get theme-aware colors to fix visibility in Dark Mode
                val tv = TypedValue()
                requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, tv, true)
                val colorOnSurface = tv.data
                
                requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurfaceVariant, tv, true)
                val colorOnSurfaceVariant = tv.data

                items.forEach { cartItem ->
                    val summaryRow = LinearLayout(requireContext()).apply {
                        orientation = LinearLayout.HORIZONTAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply { setMargins(0, 0, 0, (8 * resources.displayMetrics.density).toInt()) }
                    }

                    val tvName = TextView(requireContext()).apply {
                        text = "${cartItem.product.name} x${cartItem.quantity}"
                        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        setTextColor(colorOnSurface)
                        textSize = 14f
                    }

                    val tvPrice = TextView(requireContext()).apply {
                        text = "$${String.format("%.2f", cartItem.product.price * cartItem.quantity)}"
                        setTextColor(colorOnSurface)
                        textSize = 14f
                    }

                    summaryRow.addView(tvName)
                    summaryRow.addView(tvPrice)
                    binding.llOrderSummary.addView(summaryRow)
                }

                // Divider
                val divider = View(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (1 * resources.displayMetrics.density).toInt()
                    ).apply { setMargins(0, (16 * resources.displayMetrics.density).toInt(), 0, (16 * resources.displayMetrics.density).toInt()) }
                    setBackgroundColor(colorOnSurfaceVariant)
                    alpha = 0.2f
                }
                binding.llOrderSummary.addView(divider)

                // Total Row
                val totalRow = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }

                val tvTotalLabel = TextView(requireContext()).apply {
                    text = "Total Payable"
                    textSize = 16f
                    setTypeface(null, android.graphics.Typeface.BOLD)
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    setTextColor(colorOnSurface)
                }

                val tvTotalAmount = TextView(requireContext()).apply {
                    id = R.id.tvCheckoutTotal
                    text = "$${String.format("%.2f", viewModel.totalPrice.value)}"
                    textSize = 18f
                    setTypeface(null, android.graphics.Typeface.BOLD)
                    setTextColor(requireContext().getColor(R.color.primary))
                }

                totalRow.addView(tvTotalLabel)
                totalRow.addView(tvTotalAmount)
                binding.llOrderSummary.addView(totalRow)
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
            .create()

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
            preferenceManager.saveAddress(newAddress)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
