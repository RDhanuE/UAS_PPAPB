package com.example.calorify20.mainApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calorify20.auth.LogRegScreen
import com.example.calorify20.databinding.ActivityWelcomeScreenBinding

class WelcomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            welcomeButtonRegister.setOnClickListener{
                val intent = Intent(this@WelcomeScreen, LogRegScreen::class.java).apply { putExtra("TAB", 0) }
                startActivity(intent)
            }

            welcomeButtonLogin.setOnClickListener{
                val intent = Intent(this@WelcomeScreen, LogRegScreen::class.java).apply { putExtra("TAB", 1) }
                startActivity(intent)
            }
        }
    }
}