package com.example.islamiapp.ui.home.tabs.hadeth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.islamiapp.databinding.FragmentHadethBinding
import com.example.islamiapp.model.Hadeth
import com.example.islamiapp.ui.Constants

class HadethFragment : Fragment() {
    private lateinit var binding: FragmentHadethBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHadethBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    lateinit var adapter: HadethAdapter
    private fun initViews() {
        adapter = HadethAdapter(null)
        adapter.onItemClickListener = HadethAdapter.OnItemClickListener { position, hadeth ->
            showHadethDetails(hadeth)
        }
        binding.recyclerView.adapter = adapter

    }

    private fun showHadethDetails(hadeth: Hadeth) {
        val intent = Intent(activity, HadethDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_HADETH, hadeth)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        loadHadethFile()
        bindHadethList()
    }

    private fun bindHadethList() {
        adapter.bindItems(hadethList)
    }

    val hadethList = mutableListOf<Hadeth>()
    private fun loadHadethFile() {
        val assetManager = requireActivity().assets
        val fileName = "ahadeth.txt"
        val inputStream = assetManager.open(fileName)
        val fileContent = inputStream.bufferedReader().use { it.readText() }
        val singleHadethList = fileContent.trim().split("#")
        singleHadethList.forEach { element ->
            val lines = element.trim().split("\n")
            val title = lines[0]
            val content = lines.joinToString("\n")
            val hadeth = Hadeth(title, content)
            hadethList.add(hadeth)
        }
    }
}