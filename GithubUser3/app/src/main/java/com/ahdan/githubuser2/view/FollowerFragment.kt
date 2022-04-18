package com.ahdan.githubuser2.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahdan.githubuser2.databinding.FragmentFollowerBinding
import com.ahdan.githubuser2.model.FollowerResponseItem
import com.ahdan.githubuser2.model.adapter.FollowerAdapter
import com.ahdan.githubuser2.model.adapter.SectionsPagerAdapter
import com.ahdan.githubuser2.viewModel.FollowerViewModel
import java.util.Arrays
import kotlin.collections.ArrayList

class FollowerFragment : Fragment() {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvFollower: RecyclerView

    private val viewModel: FollowerViewModel by viewModels()

    private var dataLogin: String? = null

    private val list = ArrayList<FollowerResponseItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        rvFollower = binding.rvUserFollower
        rvFollower.setHasFixedSize(true)
        showFollowerList(list)

        dataLogin = arguments?.getString(SectionsPagerAdapter.USER_LOGIN)
        if (dataLogin != null) {
            viewModel.setListFollower(dataLogin!!)
        }

        viewModel.listFollower.observe(viewLifecycleOwner) {
            showFollowerList(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun showFollowerList(followerData: ArrayList<FollowerResponseItem>){
        Log.d("FllwerFragment_Recycler",  Arrays.toString(followerData.toArray()))
        rvFollower.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FollowerAdapter(followerData)
        rvFollower.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarFollower.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}