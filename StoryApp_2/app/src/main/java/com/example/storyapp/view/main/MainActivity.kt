package com.example.storyapp.view.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.model.UserPreference
import com.example.storyapp.view.maps.MapsActivity
import com.example.storyapp.view.upload.UploadActivity
import com.example.storyapp.view.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels{
        ViewModelFactory(this, UserPreference.getInstance(dataStore))
    }

    private lateinit var rvStories: RecyclerView

    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()

        rvStories = binding.rvStories
        rvStories.setHasFixedSize(true)
        showStoriesList()
    }

    private fun showStoriesList() {
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvStories.layoutManager = GridLayoutManager(this, 2)
        }
        else{
            rvStories.layoutManager = LinearLayoutManager(this)
        }
        val adapter = StoriesAdapter()
        rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        mainViewModel.getUser().observe(this) { user ->
            mainViewModel.stories(user.token).observe(this) {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
//        mainViewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreference.getInstance(dataStore))
//        )[MainViewModel::class.java]
        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            else{
                token = user.token
            }
        }
    }

    private fun setupAction(){
        binding.logoutButton.setOnClickListener{
            mainViewModel.logout()
        }
        binding.uploadButton.setOnClickListener{
            val intent = Intent(this, UploadActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }
        binding.fabMaps.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }
    }
}