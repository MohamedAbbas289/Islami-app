package com.example.islamiapp.ui.home.tabs.hadeth

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.islamiapp.databinding.ActivityHadethDetailsBinding
import com.example.islamiapp.model.Hadeth
import com.example.islamiapp.ui.Constants

class HadethDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityHadethDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHadethDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initParams()
        bindHadeth()
    }

    private fun initView() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun bindHadeth() {
        binding.hadethName.text = hadeth?.title
        binding.hadethContent.text = hadeth?.content
    }

    var hadeth: Hadeth? = null
    private fun initParams() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hadeth = intent.getParcelableExtra(Constants.EXTRA_HADETH, Hadeth::class.java)
        } else {
            hadeth = intent.getParcelableExtra(Constants.EXTRA_HADETH) as Hadeth?
        }
    }
}