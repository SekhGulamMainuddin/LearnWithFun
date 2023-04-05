package com.sekhgmainuddin.learnwithfun.ui.home.student.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sekhgmainuddin.learnwithfun.data.modals.Banner
import com.sekhgmainuddin.learnwithfun.data.modals.MyLearnings
import com.sekhgmainuddin.learnwithfun.databinding.BannerViewpagerBinding
import com.sekhgmainuddin.learnwithfun.databinding.LearningsLayoutBinding

const val TYPE_BANNER = 0
const val TYPE_LEARNINGS = 1

class MyLearningsAdapter(val context: Context) :
    ListAdapter<MyLearnings, ViewHolder>(DiffCallBack()) {

    private var banner: Banner = Banner()

    fun updateBanner(newBanner: Banner) {
        banner = newBanner
        notifyItemChanged(0)
    }


    private class DiffCallBack : DiffUtil.ItemCallback<MyLearnings>() {
        override fun areItemsTheSame(oldItem: MyLearnings, newItem: MyLearnings): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MyLearnings, newItem: MyLearnings): Boolean {
            return oldItem == newItem
        }

    }

    private inner class BannerViewHolder(
        private val binding: BannerViewpagerBinding,
        context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var bannerAdapter: BannerAdapter

        init {
            bannerAdapter = BannerAdapter(context)
            binding.apply {
                bannerViewPager.adapter = bannerAdapter
                TabLayoutMediator(
                    bannerTabLayout, bannerViewPager
                ) { _: TabLayout.Tab?, _: Int ->
                }.attach()
            }

        }

        fun bind(banner: Banner) {
            bannerAdapter.updateBannerImages(banner)
            Toast.makeText(context, "Updated $banner", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class LearningsViewHolder(
        private val binding: LearningsLayoutBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 0)
            BannerViewHolder(
                BannerViewpagerBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                ), context
            )
        else
            LearningsViewHolder(
                LearningsLayoutBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                ), context
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position==0) {
            (holder as BannerViewHolder).bind(banner)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_BANNER else TYPE_LEARNINGS
    }

}