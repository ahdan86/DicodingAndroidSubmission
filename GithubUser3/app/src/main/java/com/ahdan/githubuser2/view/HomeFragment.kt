package com.ahdan.githubuser2.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahdan.githubuser2.model.adapter.SearchAdapter
import com.ahdan.githubuser2.model.ItemsItem
import com.ahdan.githubuser2.R
import com.ahdan.githubuser2.viewModel.MainViewModel
import com.ahdan.githubuser2.databinding.FragmentHomeBinding
import com.ahdan.githubuser2.preferences.SettingPreferences
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvUser: RecyclerView
    private val list = ArrayList<ItemsItem>()

    private lateinit var viewModel: MainViewModel

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
        val pref = context?.let { SettingPreferences.getInstance(it.dataStore) }
        MainViewModel(pref!!).also { viewModel = it }

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

        val switchTheme =  binding.switchTheme

        viewModel.listUser.observe(viewLifecycleOwner) { listUser ->
            showUserList(listUser)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.getThemeSettings().observe(viewLifecycleOwner
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu1 -> {
                NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_menuActivity)
                true
            }
            R.id.menu2 -> {
                NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_favoriteFragment)
                true
            }
            else -> true
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
                Log.d("HomeFragment_Recycler",  "Clicked")
                setSelectedUser(data)
            }
        })
    }

    companion object {
        const val USER_NAME = "user_name"
    }

    private fun setSelectedUser(data: ItemsItem){
        val mBundle = Bundle()
        mBundle.putString(USER_NAME, data.login)
        findNavController().safeNavigate(R.id.homeFragment, R.id.action_homeFragment_to_detailFragment, mBundle)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun NavController.safeNavigate(
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