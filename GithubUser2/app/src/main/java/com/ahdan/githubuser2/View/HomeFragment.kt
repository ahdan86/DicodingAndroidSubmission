package com.ahdan.githubuser2.View

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahdan.githubuser2.Model.Adapter.SearchAdapter
import com.ahdan.githubuser2.Model.ItemsItem
import com.ahdan.githubuser2.R
import com.ahdan.githubuser2.ViewModel.MainViewModel
import com.ahdan.githubuser2.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvUser: RecyclerView
    private val list = ArrayList<ItemsItem>()

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(false)

        rvUser = binding.rvUser
        rvUser.setHasFixedSize(true)
        showUserList(list)

        val searchView = binding.searchUser
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty()!!) {
                    newText.let { viewModel.setFindUser(it)}
                    showLoading(true)
                } else {
                    showLoading(false)
                    list.clear()
                }
                return true
            }
        })

        viewModel.listUser.observe(viewLifecycleOwner) { listUser ->
            showUserList(listUser)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun showUserList(userData: ArrayList<ItemsItem>){
        Log.d("HomeFragment_Recycler",  Arrays.toString(userData.toArray()))
        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvUser.layoutManager = GridLayoutManager(requireContext(), 2)
        }
        else{
            rvUser.layoutManager = LinearLayoutManager(requireContext())
        }
        val adapter = SearchAdapter(userData)
        Log.d("HomeFragment_Recycler",  adapter.itemCount.toString())
        rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object: SearchAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                setSelectedUser(data)
            }
        })
    }

    companion object {
        val USER_NAME = "user_name"
    }

    private fun setSelectedUser(data: ItemsItem){
        val mBundle = Bundle()
        mBundle.putString(USER_NAME, data.login)
        findNavController().safeNavigate(R.id.homeFragment, R.id.action_homeFragment_to_detailFragment, mBundle)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
    }

    fun NavController.safeNavigate(
        @IdRes currentDestinationId: Int,
        @IdRes id: Int,
        args: Bundle? = null
    ) {
        if (currentDestinationId == currentDestination?.id) {
            navigate(id, args)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}