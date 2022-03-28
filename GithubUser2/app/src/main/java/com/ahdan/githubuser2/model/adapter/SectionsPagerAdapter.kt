package com.ahdan.githubuser2.model.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ahdan.githubuser2.view.FollowerFragment
import com.ahdan.githubuser2.view.FollowingFragment
import com.ahdan.githubuser2.view.HomeFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private lateinit var login: String

    companion object{
        const val USER_LOGIN = "user_login"
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowerFragment()
                val mBundle = Bundle()
                mBundle.putString(USER_LOGIN, login)
                fragment.arguments = mBundle
            }
            1 -> {
                fragment = FollowingFragment()
                val mBundle = Bundle()
                mBundle.putString(USER_LOGIN, login)
                fragment.arguments = mBundle
            }
        }
        return fragment as Fragment
    }

    fun setLogin(login: String){
        this.login = login
    }
}