package com.ahdan.githubuser2.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.ahdan.githubuser2.Model.Adapter.SectionsPagerAdapter
import com.ahdan.githubuser2.Model.UserDetailResponse
import com.ahdan.githubuser2.R
import com.ahdan.githubuser2.ViewModel.DetailViewModel
import com.ahdan.githubuser2.databinding.FragmentDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userLogin = DetailFragmentArgs.fromBundle(arguments as Bundle).login
        viewModel.setDetailUser(userLogin)

//        val sectionsPagerAdapter = context?.let {
//            SectionsPagerAdapter(it as AppCompatActivity)
//        }
//        val viewPager: ViewPager2 = binding.viewPager
//        viewPager.adapter = sectionsPagerAdapter
//        val tabs: TabLayout = binding.tabs
//        TabLayoutMediator(tabs, viewPager) { tab, position ->
//            tab.text = resources.getString(TAB_TITLES[position])
//        }.attach()

        viewModel.user.observe(viewLifecycleOwner){
            showDetailUser(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

    }

    private fun showDetailUser(userDetail: UserDetailResponse){
        Log.d("DetailFragment_User", userDetail.toString())
        Glide.with(this)
            .load(userDetail.avatarUrl)
            .apply(RequestOptions().override(250, 250))
            .into(binding.imageView)
        binding.apply{
            name.text = userDetail.name
            idNo.text = userDetail.id.toString()
            idFollower.text = resources.getString(R.string.detil_follower, userDetail.followers)
            idFollowing.text =resources.getString(R.string.detil_following, userDetail.following)
            idRepo.text = resources.getString(R.string.detil_repository, userDetail.publicRepos)
            idCompany.text = userDetail.company
            idLocation.text = userDetail.location
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}