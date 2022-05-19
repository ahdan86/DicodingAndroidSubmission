package com.example.storyapp.view.main

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyapp.databinding.ItemUserPhotoBinding
import com.example.storyapp.model.ListStoryItem
import com.example.storyapp.view.detail.DetailActivity

class MainAdapter(private val listStories: List<ListStoryItem>): RecyclerView.Adapter<MainAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemUserPhotoBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listStories[position]
        Glide.with(holder.itemView.context)
            .load(data.photoUrl)
            .apply(RequestOptions().override(350, 150))
            .into(holder.binding.imgItemPhoto)
        holder.binding.textView.text = data.name
        holder.binding.miniDescTextView.text = data.description
//        holder.itemView.blockingClickListener {
//            onItemClickCallback.onItemClicked(listStories[holder.adapterPosition])
//        }
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("Story", data)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    it.context as Activity,
                    Pair(holder.binding.imgItemPhoto, "profile"),
                    Pair(holder.binding.textView, "name"),
                    Pair(holder.binding.miniDescTextView, "description")
                )
            it.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    override fun getItemCount() = listStories.size
}