package com.example.islamiapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.islamiapp.databinding.ActivitySplashBinding
import com.example.islamiapp.ui.home.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startHomeActivity()
    }

    private fun startHomeActivity() {
        lifecycleScope.launch {
            delay(SPLASH_DURATION_MILLIS)
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
        }
    }

    private companion object {
        const val SPLASH_DURATION_MILLIS = 1_200L
    }
}
