package com.example.storyapp.view.register

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
//        setMyButtonEnable()
        setupAction()
    }

//    private fun setMyButtonEnable() {
//        val result = binding.passwordEditText.text
//        binding.signupButton.isEnabled = result != null && result.toString().length >= 6
//    }

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

    private fun setupAction(){
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailRegisterEditText.text.toString()
            val password = binding.passwordRegisterEditText.text.toString()
            when {
                name.isEmpty() -> {
                    Toast.makeText(this, "Harap Isi Nama", Toast.LENGTH_SHORT).show()
                }
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
                    registerViewModel.postUser(name, email, password)
                    if(registerViewModel.getRegisterSuccess()) {
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Akunnya sudah dibuat.")
                            setPositiveButton("Lanjut") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    else{
                        AlertDialog.Builder(this).apply {
                            setTitle("Oops!")
                            setMessage("Akunnya gagal dibuat.")
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