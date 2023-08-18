package com.example.islamiapp.ui.home.tabs.tasbeh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.islamiapp.databinding.FragmentTasbehBinding

class TasbehFragment : Fragment() {
    private lateinit var binding: FragmentTasbehBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasbehBinding.inflate(inflater, container, false)
        return binding.root
    }
}