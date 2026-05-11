package com.example.minigrocerydeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minigrocerydeliveryapp.databinding.ItemAddressBinding
import com.example.minigrocerydeliveryapp.model.UserAddress

class AddressAdapter(
    private val onDelete: (UserAddress) -> Unit
) : ListAdapter<UserAddress, AddressAdapter.AddressViewHolder>(AddressDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AddressViewHolder(private val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: UserAddress) {
            binding.tvName.text = address.name
            binding.tvAddress.text = address.toDisplayString()
            binding.tvPhone.text = address.phone
            binding.btnDelete.setOnClickListener { onDelete(address) }
        }
    }

    class AddressDiffCallback : DiffUtil.ItemCallback<UserAddress>() {
        override fun areItemsTheSame(oldItem: UserAddress, newItem: UserAddress): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: UserAddress, newItem: UserAddress): Boolean =
            oldItem == newItem
    }
}
