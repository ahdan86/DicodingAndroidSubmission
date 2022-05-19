package com.example.storyapp.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupviewModel()
        setupAction()

        showLoading(false)
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(it: Boolean?) {
        binding.progressBar.visibility = if (it == true) View.VISIBLE else View.GONE
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

    private fun setupviewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun setupAction(){
        binding.loginButton.setOnClickListener{
            val email = binding.emailLoginEditText.text.toString()
            val password = binding.passwordLoginEditText.text.toString()
            when{
                email.isEmpty() -> {
                    Toast.makeText(this, "Harap Isi Email", Toast.LENGTH_SHORT).show()
                }
                email.isNotEmpty() && !email.contains("@") -> {
                    Toast.makeText(this, "Harap Isi Email Yang Valid", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Harap Isi Password", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(this, "Password Minimal 6 Karakter", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    loginViewModel.postLogin(email, password)

                    loginViewModel.loginSuccess.observe(this){
                        if(it) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        else if(!it){
                            AlertDialog.Builder(this).apply {
                                setTitle("Oops!")
                                setMessage("Email/Password Salah.")
                                setPositiveButton("Coba lagi") { _, _ ->
                                // do nothing
                                }
                                create()
                                show()
                            }
                        }
                    }
                }
            }
        }
    }
}