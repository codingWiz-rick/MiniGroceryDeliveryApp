package com.example.minigrocerydeliveryapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minigrocerydeliveryapp.databinding.FragmentOrdersBinding
import com.example.minigrocerydeliveryapp.ui.adapters.OrderAdapter
import com.example.minigrocerydeliveryapp.viewmodel.GroceryViewModel
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GroceryViewModel by activityViewModels()
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeOrders()
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter()
        binding.rvOrders.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeOrders() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orders.collect { orders ->
                if (orders.isEmpty()) {
                    binding.rvOrders.visibility = View.GONE
                    binding.tvNoOrders.visibility = View.VISIBLE
                } else {
                    binding.rvOrders.visibility = View.VISIBLE
                    binding.tvNoOrders.visibility = View.GONE
                    orderAdapter.submitList(orders)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
