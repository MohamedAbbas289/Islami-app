package com.example.islamiapp.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.islamiapp.R
import com.example.islamiapp.databinding.ActivityHomeBinding
import com.example.islamiapp.domain.model.ThemeMode
import com.example.islamiapp.ui.common.ViewModelFactory
import com.example.islamiapp.ui.common.appContainer
import com.example.islamiapp.ui.home.tabs.hadeth.HadethFragment
import com.example.islamiapp.ui.home.tabs.quran.QuranFragment
import com.example.islamiapp.ui.home.tabs.radio.RadioFragment
import com.example.islamiapp.ui.home.tabs.tasbeh.TasbehFragment
import com.example.islamiapp.ui.theme.toNightMode

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory { HomeViewModel(appContainer.themeRepository) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupThemeMenu()
        setupNavigation(savedInstanceState)
    }

    private fun setupNavigation(savedInstanceState: Bundle?) {
        binding.content.bottomNav
            .setOnItemSelectedListener { item ->
                val fragment = when (item.itemId) {
                    R.id.nav_quran -> QuranFragment()
                    R.id.nav_hadeth -> HadethFragment()
                    R.id.nav_tasbeh -> TasbehFragment()
                    R.id.nav_radio -> RadioFragment()
                    else -> null
                }
                fragment?.let(::showTabFragment) != null
            }
        if (savedInstanceState == null) {
            binding.content.bottomNav.selectedItemId = R.id.nav_quran
        }
    }

    private fun showTabFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setupThemeMenu() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            val mode = when (item.itemId) {
                R.id.theme_system -> ThemeMode.SYSTEM
                R.id.theme_light -> ThemeMode.LIGHT
                R.id.theme_dark -> ThemeMode.DARK
                else -> return@setOnMenuItemClickListener false
            }
            viewModel.selectTheme(mode)
            AppCompatDelegate.setDefaultNightMode(mode.toNightMode())
            true
        }
    }
}
