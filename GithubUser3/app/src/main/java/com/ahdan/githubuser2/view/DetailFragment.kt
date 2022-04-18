package com.ahdan.githubuser2.view

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.ahdan.githubuser2.model.UserDetailResponse
import com.ahdan.githubuser2.R
import com.ahdan.githubuser2.database.Favorite
import com.ahdan.githubuser2.viewModel.DetailViewModel
import com.ahdan.githubuser2.databinding.FragmentDetailBinding
import com.ahdan.githubuser2.model.adapter.SectionsPagerAdapter
import com.ahdan.githubuser2.view.insert.FavoriteAddUpdateViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment(),View.OnClickListener {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private var favorite: Favorite? = null
    private lateinit var favoriteAddUpdateViewModel: FavoriteAddUpdateViewModel

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    private var dataLogin: String? = null
    private var dataImage: String? = null

    private var checkExist: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAddUpdateViewModel =
            FavoriteAddUpdateViewModel(activity?.applicationContext as Application)
        favorite = Favorite()

        dataLogin = arguments?.getString(HomeFragment.USER_NAME)
        if (dataLogin != null) {
            viewModel.setDetailUser(dataLogin!!)
        }

        val sectionsPagerAdapter = context?.let {
            SectionsPagerAdapter(it as AppCompatActivity)
        }
        dataLogin?.let {
            sectionsPagerAdapter?.setLogin(it)
        }

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        checkExist = dataLogin?.let { it1 -> isExist(it1) }
        if(checkExist == true){
            binding.fabAdd.setImageResource(R.drawable.ic_baseline_delete_24)
        } else{
            binding.fabAdd.setImageResource(R.drawable.ic_add)
        }

        viewModel.user.observe(viewLifecycleOwner) {
            showDetailUser(it)
            binding.fabAdd.setOnClickListener(this)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun isExist(login: String): Boolean {
        return favoriteAddUpdateViewModel.isExist(login)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_add -> {
                checkExist = dataLogin?.let { it1 -> isExist(it1) }
                if (!checkExist!!) {
                    favorite?.let {
                        favorite?.login = dataLogin
                        favorite?.avatar_url = dataImage
                        favoriteAddUpdateViewModel.insert(favorite as Favorite)
                    }
                    binding.fabAdd.setImageResource(R.drawable.ic_baseline_delete_24)
                } else {
                    favoriteAddUpdateViewModel.deleteById(dataLogin!!)
                    binding.fabAdd.setImageResource(R.drawable.ic_add)
                }
            }
        }
    }

    private fun showDetailUser(userDetail: UserDetailResponse) {
        Log.d("DetailFragment_User", userDetail.toString())
        dataImage = userDetail.avatarUrl
        Glide.with(this)
            .load(userDetail.avatarUrl)
            .apply(RequestOptions().override(250, 250))
            .into(binding.imageView)
        binding.apply {
            name.text = userDetail.name
            idNo.text = userDetail.id.toString()
            idFollower.text = resources.getString(R.string.detil_follower, userDetail.followers)
            idFollowing.text = resources.getString(R.string.detil_following, userDetail.following)
            idRepo.text = resources.getString(R.string.detil_repository, userDetail.publicRepos)
            if (userDetail.company != null) {
                idCompany.text = userDetail.company
            } else {
                idCompany.text = resources.getString(R.string.not_found)
            }
            if (userDetail.location != null) {
                idLocation.text = userDetail.location
            } else {
                idLocation.text = resources.getString(R.string.not_found)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

