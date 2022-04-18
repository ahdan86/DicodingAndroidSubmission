package com.ahdan.githubuser2.model.adapter

import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahdan.githubuser2.database.Favorite
import com.ahdan.githubuser2.databinding.ItemFavoriteRowBinding
import com.ahdan.githubuser2.helper.FavoriteDiffCallBack
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorite = ArrayList<Favorite>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setListFavorite(listFavorite: ArrayList<Favorite>){
        val diffCallBack = FavoriteDiffCallBack(this.listFavorite, listFavorite)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorite)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val data = listFavorite[position]
        Glide.with(holder.itemView.context)
            .load(data.avatar_url)
            .apply(RequestOptions().override(250, 250))
            .into(holder.binding.imgItemPhoto)
        holder.binding.tvUserName.text = data.login
        holder.itemView.blockingClickListener {
            onItemClickCallback.onItemClicked(listFavorite[holder.adapterPosition])
        }
    }

    private val clickTag = "__click__"
    fun View.blockingClickListener(debounceTime: Long = 1200L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                val timeNow = SystemClock.elapsedRealtime()
                val elapsedTimeSinceLastClick = timeNow - lastClickTime
                Log.d(
                    clickTag, """
                        DebounceTime: $debounceTime
                        Time Elapsed: $elapsedTimeSinceLastClick
                        Is within debounce time: ${elapsedTimeSinceLastClick < debounceTime}
                    """.trimIndent()
                )

                if (elapsedTimeSinceLastClick < debounceTime) {
                    Log.d(clickTag, "Double click shielded")
                    return
                } else {
                    Log.d(clickTag, "Click happened")
                    action()
                }
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }

    class FavoriteViewHolder(var binding: ItemFavoriteRowBinding): RecyclerView.ViewHolder(binding.root)
}