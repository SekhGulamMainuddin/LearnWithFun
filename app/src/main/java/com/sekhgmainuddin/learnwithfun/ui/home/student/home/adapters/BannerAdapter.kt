package com.sekhgmainuddin.learnwithfun.ui.home.student.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.sekhgmainuddin.learnwithfun.data.modals.Banner
import com.sekhgmainuddin.learnwithfun.databinding.BannerLayoutBinding

class BannerAdapter(val context: Context): RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    private val bannerImages= ArrayList<String>()

    fun updateBannerImages(newBanner: Banner){
        bannerImages.clear()
        bannerImages.addAll(newBanner.list)
        notifyDataSetChanged()
    }

    class BannerViewHolder(private val binding: BannerLayoutBinding, private val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String){
            Glide.with(context).load(url).into(binding.bannerImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(BannerLayoutBinding.inflate(LayoutInflater.from(context), parent, false), context)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerImages[position])
    }

    override fun getItemCount(): Int {
        return bannerImages.size
    }

}