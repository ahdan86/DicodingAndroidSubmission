package com.ahdan.githubuser2.Model.Adapter

import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahdan.githubuser2.Model.ItemsItem
import com.ahdan.githubuser2.databinding.ItemUserRowBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.collections.ArrayList

class SearchAdapter(private val listSearchUser: ArrayList<ItemsItem>): RecyclerView.Adapter<SearchAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemUserRowBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listSearchUser[position]
        Log.d("SearchAdapter",  data.login)
        Glide.with(holder.itemView.context)
            .load(data.avatarUrl)
            .apply(RequestOptions().override(250, 250))
            .into(holder.binding.imgItemPhoto)
        holder.binding.tvUserName.text = data.login
        holder.binding.tvId.text = data.id.toString()
        holder.binding.tvType.text = data.type
        holder.itemView.blockingClickListener {
            onItemClickCallback.onItemClicked(listSearchUser[holder.adapterPosition])
        }
    }

    override fun getItemCount() = listSearchUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    private val clickTag = "__click__"
    fun View.blockingClickListener(debounceTime: Long = 1200L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                val timeNow = SystemClock.elapsedRealtime()
                val elapsedTimeSinceLastClick = timeNow - lastClickTime
                Log.d(clickTag, """
                        DebounceTime: $debounceTime
                        Time Elapsed: $elapsedTimeSinceLastClick
                        Is within debounce time: ${elapsedTimeSinceLastClick < debounceTime}
                    """.trimIndent())

                if (elapsedTimeSinceLastClick < debounceTime) {
                    Log.d(clickTag, "Double click shielded")
                    return
                }
                else {
                    Log.d(clickTag, "Click happened")
                    action()
                }
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }
}