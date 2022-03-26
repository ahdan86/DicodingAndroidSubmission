package com.ahdan.githubuser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahdan.githubuser.databinding.ActivityDetailBinding

class Detail : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>("DATA") as User
        binding.apply {
            name.text = user.name
            imageView.setImageResource(user.avatar)
            idUsername.text = user.username
            idFollower.text = "${user.followers} Followers"
            idFollowing.text = "${user.following} Following"
            idRepo.text = "${user.repository} Repositories"
            idCompany.text = user.company
            idLocation.text = user.location
        }

    }
}