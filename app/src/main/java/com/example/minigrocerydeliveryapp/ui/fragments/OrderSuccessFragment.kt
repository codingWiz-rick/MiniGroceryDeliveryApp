package com.example.minigrocerydeliveryapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.minigrocerydeliveryapp.R
import com.example.minigrocerydeliveryapp.databinding.FragmentOrderSuccessBinding
import com.example.minigrocerydeliveryapp.viewmodel.GroceryViewModel
import kotlin.random.Random

class OrderSuccessFragment : Fragment() {

    private var _binding: FragmentOrderSuccessBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroceryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val latestOrder = viewModel.orders.value.firstOrNull()
        
        if (latestOrder != null) {
            binding.tvOrderId.text = "Order ID: #${latestOrder.id}"
            populateSummary(latestOrder.items)
        } else {
            val orderId = Random.nextInt(10000, 99999)
            binding.tvOrderId.text = "Order ID: #ORD-$orderId"
        }

        binding.btnViewOrders.setOnClickListener {
            findNavController().navigate(R.id.ordersFragment)
        }

        binding.btnBackToHome.setOnClickListener {
            findNavController().navigate(R.id.action_orderSuccessFragment_to_homeFragment)
        }
    }

    private fun populateSummary(items: List<com.example.minigrocerydeliveryapp.model.CartItem>) {
        items.forEach { cartItem ->
            val textView = TextView(requireContext()).apply {
                text = "${cartItem.product.name} x${cartItem.quantity}"
                setTextColor(requireContext().getColor(R.color.text_main))
                setPadding(0, 4, 0, 4)
            }
            binding.llOrderSummary.addView(textView)
        }
        
        val totalView = TextView(requireContext()).apply {
            val total = items.sumOf { it.product.price * it.quantity }
            text = "Total: $${String.format("%.2f", total)}"
            textStyle = android.graphics.Typeface.BOLD
            setTextColor(requireContext().getColor(R.color.primary))
            setPadding(0, 16, 0, 0)
        }
        binding.llOrderSummary.addView(totalView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private var TextView.textStyle: Int
        get() = typeface.style
        set(value) {
            setTypeface(null, value)
        }
}
