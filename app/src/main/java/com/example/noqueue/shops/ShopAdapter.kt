package com.example.noqueue.shops

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.noqueue.R
import com.example.noqueue.databinding.ShopLayoutBinding

class ShopAdapter(private var shopsList : ArrayList<Shop>,
private val shopsViewModel: ShopsViewModel): ListAdapter<Shop, ShopAdapter.ShopViewHolder>(ShopDiffCallback()) {

    class ShopDiffCallback : DiffUtil.ItemCallback<Shop>() {
        override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem.name === newItem.name
        }

        override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem == newItem
        }
    }
    inner class ShopViewHolder(private val shopBinding : ShopLayoutBinding) : RecyclerView.ViewHolder(shopBinding.root){
        fun bind(shop : Shop){
            Glide.with(shopBinding.root).load(shop.imgUrl).override(800,400).fitCenter().placeholder(R.drawable.placeholder).override(800,400)
                .fitCenter().into(shopBinding.shopImage)

            shopBinding.root.setOnClickListener {
                Navigation.findNavController(shopBinding.root).navigate(R.id.action_shopsFragment_to_cartFragment, bundleOf("shopName" to shop.name))
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding =
            ShopLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        shopsList = shopsViewModel.shopsList.value!!
        submitList(shopsList)
        return ShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shopsList[position]
        holder.bind(shop)
    }
    }