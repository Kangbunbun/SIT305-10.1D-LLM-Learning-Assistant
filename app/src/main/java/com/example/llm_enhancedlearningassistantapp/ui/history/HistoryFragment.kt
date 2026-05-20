package com.example.llm_enhancedlearningassistantapp.ui.history

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.llm_enhancedlearningassistantapp.data.HistoryPrefs
import com.example.llm_enhancedlearningassistantapp.databinding.FragmentHistoryBinding
import com.example.llm_enhancedlearningassistantapp.ui.BaseFragment

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {

    private lateinit var historyPrefs: HistoryPrefs
    private lateinit var historyAdapter: HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyPrefs = HistoryPrefs(requireContext())

        setupRecyclerView()
        setupButtons()
        loadHistory()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter()

        binding.rvHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupButtons() {
        binding.btnBackHome.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadHistory() {
        val items = historyPrefs.getHistoryItems()

        if (items.isEmpty()) {
            binding.tvEmptyHistory.visibility = View.VISIBLE
            binding.rvHistory.visibility = View.GONE
        } else {
            binding.tvEmptyHistory.visibility = View.GONE
            binding.rvHistory.visibility = View.VISIBLE
            historyAdapter.submitList(items)
        }
    }
}