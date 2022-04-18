package com.ahdan.githubuser2.model.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahdan.githubuser2.databinding.ItemFollowerLayoutBinding
import com.ahdan.githubuser2.model.FollowerResponseItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FollowerAdapter(private val listFollower: ArrayList<FollowerResponseItem>): RecyclerView.Adapter<FollowerAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemFollowerLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerAdapter.ListViewHolder {
        val binding = ItemFollowerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listFollower[position]
        Log.d("FollowerAdapter",  data.login)
        Glide.with(holder.itemView.context)
            .load(data.avatarUrl)
            .apply(RequestOptions().override(250, 250))
            .into(holder.binding.imgItemPhotoFollower)
        holder.binding.tvUserNameFollower.text = data.login
        holder.binding.tvIdFollower.text = data.id.toString()
        holder.binding.tvTypeFollower.text = data.type
    }

    override fun getItemCount() = listFollower.size
}