package com.ahdan.githubuser

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahdan.githubuser.databinding.ItemRowUserBinding

class UserAdapter(private val listUser: ArrayList<User>): RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, name, company, photo) = listUser[position]
        Log.d("UserAdapter", listUser[position].name)
        holder.binding.imgItemPhoto.setImageResource(photo)
        holder.binding.tvName.text = name
        holder.binding.tvUserName.text = username
        holder.binding.tvCompany.text = company
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount() = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

}