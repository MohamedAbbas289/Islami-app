package com.example.islamiapp.ui.home.tabs.tasbeh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.islamiapp.databinding.FragmentTasbehBinding

class TasbehFragment : Fragment() {
    private lateinit var binding: FragmentTasbehBinding
    var counter: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasbehBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sebhaBtn.setOnClickListener {
            binding.sebhaBody.rotation += 30f
            clickSebhaBtn()
        }

    }

    private fun clickSebhaBtn() {
        if (counter == 33 && binding.sebhaBtn.text == "سبحان الله") {
            counter = 0
            binding.counter.text = counter.toString()
            binding.sebhaBtn.text = "الحمد لله"
        } else if (counter == 33 && binding.sebhaBtn.text == "الحمد لله") {
            counter = 0
            binding.counter.text = counter.toString()
            binding.sebhaBtn.text = "الله اكبر"
        } else if (counter == 33 && binding.sebhaBtn.text == "الله اكبر") {
            counter = 0
            binding.counter.text = counter.toString()
            binding.sebhaBtn.text = "سبحان الله"
        } else {
            counter++
            binding.counter.text = counter.toString()
        }
    }
}