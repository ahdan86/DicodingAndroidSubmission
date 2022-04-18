package com.ahdan.githubuser2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahdan.githubuser2.R
import com.ahdan.githubuser2.database.Favorite
import com.ahdan.githubuser2.databinding.FragmentFavoriteBinding
import com.ahdan.githubuser2.model.adapter.FavoriteAdapter
import com.ahdan.githubuser2.viewModel.FavoriteViewModel

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var rvFavorite: RecyclerView

    private val list = ArrayList<Favorite>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        viewModel = FavoriteViewModel(requireActivity().application)

        rvFavorite = binding.rvUser
        rvFavorite.setHasFixedSize(true)
        showListFavorite(list)

        viewModel.getAllFavorites().observe(viewLifecycleOwner) {
            showListFavorite(it as ArrayList<Favorite>)
        }
    }

    companion object {
        const val USER_NAME = "user_name"
    }

    private fun setSelectedUser(data: Favorite) {
        val mBundle = Bundle()
        mBundle.putString(USER_NAME, data.login)
        NavHostFragment.findNavController(this).navigate(R.id.action_favoriteFragment_to_detailFragment, mBundle)
    }

    private fun showListFavorite(user: ArrayList<Favorite>) {
        rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FavoriteAdapter()
        adapter.setListFavorite(user)
        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favorite) {
                setSelectedUser(data)
            }
        })
        rvFavorite.adapter= adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}