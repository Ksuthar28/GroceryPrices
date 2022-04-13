package com.sample.marketprices.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.marketprices.R
import com.sample.marketprices.databinding.ItemMarketBinding
import com.sample.marketprices.models.Market
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class MarketAdapter @Inject constructor(): RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Market>() {
        override fun areItemsTheSame(oldItem: Market, newItem: Market): Boolean {
            return oldItem.market == newItem.market
        }

        override fun areContentsTheSame(oldItem: Market, newItem: Market): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

//    var restaurants: List<Restaurant>? = null
//        @SuppressLint("NotifyDataSetChanged")
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MarketViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_market,
            parent,
            false
        )
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.binding.market = differ.currentList[position]
    }

    inner class MarketViewHolder(val binding: ItemMarketBinding) :
        RecyclerView.ViewHolder(binding.root)
}