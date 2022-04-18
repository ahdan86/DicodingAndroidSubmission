package com.ahdan.githubuser2.model.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahdan.githubuser2.databinding.ItemFollowingLayoutBinding
import com.ahdan.githubuser2.model.FollowingResponseItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FollowingAdapter(private val listFollowing: ArrayList<FollowingResponseItem>): RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemFollowingLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemFollowingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listFollowing[position]
        Log.d("FollowingAdapter", data.login)
        Glide.with(holder.itemView.context)
            .load(data.avatarUrl)
            .apply(RequestOptions().override(250, 250))
            .into(holder.binding.imgItemPhotoFollowing)
        holder.binding.tvUserNameFollowing.text = data.login
        holder.binding.tvIdFollowing.text = data.id.toString()
        holder.binding.tvTypeFollowing.text = data.type
    }

    override fun getItemCount() = listFollowing.size
}