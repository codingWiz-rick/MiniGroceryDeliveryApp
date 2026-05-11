package com.example.minigrocerydeliveryapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.size.Scale
import com.example.minigrocerydeliveryapp.R
import com.example.minigrocerydeliveryapp.data.ProductRepository
import com.example.minigrocerydeliveryapp.databinding.FragmentProductDetailBinding
import com.example.minigrocerydeliveryapp.viewmodel.GroceryViewModel

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroceryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getInt("productId") ?: 0
        val product = ProductRepository.dummyProducts.find { it.id == productId }

        product?.let { p ->
            binding.tvDetailName.text = p.name
            binding.tvDetailCategory.text = p.category
            binding.tvDetailPrice.text = "$${p.price}"
            
            // Optimized image loading
            binding.ivProductLarge.load(p.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
                scale(Scale.FIT)
                size(800) // Slightly larger limit for detail view, but prevents massive mem usage
            }

            binding.btnAddToCartDetail.setOnClickListener {
                viewModel.addToCart(p)
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
