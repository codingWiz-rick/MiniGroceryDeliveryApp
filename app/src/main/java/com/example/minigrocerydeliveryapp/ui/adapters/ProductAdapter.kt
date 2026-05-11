package com.example.minigrocerydeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.minigrocerydeliveryapp.R
import com.example.minigrocerydeliveryapp.databinding.ItemProductBinding
import com.example.minigrocerydeliveryapp.model.Product

class ProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onAddToCart: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = "$${product.price}"
            
            // Optimized image loading
            binding.ivProduct.load(product.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background) // Fast fallback
                error(R.drawable.ic_launcher_background)
                scale(Scale.FIT) // Avoids heavy image scaling on main thread
                size(400) // Limits memory footprint significantly
            }
            
            binding.root.setOnClickListener { onProductClick(product) }
            binding.btnAdd.setOnClickListener { onAddToCart(product) }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
