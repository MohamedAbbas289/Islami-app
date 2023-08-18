package com.example.islamiapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.islamiapp.R
import com.example.islamiapp.databinding.ActivityHomeBinding
import com.example.islamiapp.ui.home.tabs.hadeth.HadethFragment
import com.example.islamiapp.ui.home.tabs.quran.QuranFragment
import com.example.islamiapp.ui.home.tabs.radio.RadioFragment
import com.example.islamiapp.ui.home.tabs.tasbeh.TasbehFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.content.bottomNav
            .setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_quran -> {
                        showTabFragment(QuranFragment())
                    }

                    R.id.nav_hadeth -> {
                        showTabFragment(HadethFragment())
                    }

                    R.id.nav_tasbeh -> {
                        showTabFragment(TasbehFragment())
                    }

                    R.id.nav_radio -> {
                        showTabFragment(RadioFragment())
                    }
                }
                true
            }
        binding.content.bottomNav.selectedItemId = R.id.nav_quran
    }

    private fun showTabFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}