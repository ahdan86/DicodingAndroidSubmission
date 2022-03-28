package com.ahdan.githubuser2.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahdan.githubuser2.databinding.FragmentFollowingBinding
import com.ahdan.githubuser2.model.FollowingResponseItem
import com.ahdan.githubuser2.model.adapter.FollowingAdapter
import com.ahdan.githubuser2.model.adapter.SectionsPagerAdapter
import com.ahdan.githubuser2.viewModel.FollowingViewModel
import java.util.Arrays
import kotlin.collections.ArrayList

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvFollowing: RecyclerView

    private val viewModel: FollowingViewModel by viewModels()

    private var dataLogin: String? = null

    private val list = ArrayList<FollowingResponseItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        rvFollowing = binding.rvUserFollowing
        rvFollowing.setHasFixedSize(true)
        showFollowingList(list)

        dataLogin = arguments?.getString(SectionsPagerAdapter.USER_LOGIN)
        if (dataLogin != null) {
            viewModel.setListFollowing(dataLogin!!)
        }

        viewModel.listFollowing.observe(viewLifecycleOwner) {
            showFollowingList(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun showFollowingList(followingData: ArrayList<FollowingResponseItem>){
        Log.d("FlwingFragment_Recycler",  Arrays.toString(followingData.toArray()))
        rvFollowing.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FollowingAdapter(followingData)
        rvFollowing.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarFollowing.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}