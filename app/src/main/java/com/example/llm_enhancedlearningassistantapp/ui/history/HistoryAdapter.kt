package com.example.llm_enhancedlearningassistantapp.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_enhancedlearningassistantapp.data.QuestionHistoryItem
import com.example.llm_enhancedlearningassistantapp.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var historyItems: List<QuestionHistoryItem> = emptyList()
    private val expandedItemIds = mutableSetOf<String>()

    fun submitList(items: List<QuestionHistoryItem>) {
        historyItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyItems[position])
    }

    override fun getItemCount(): Int = historyItems.size

    inner class HistoryViewHolder(
        private val binding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestionHistoryItem) {
            val isExpanded = expandedItemIds.contains(item.id)

            binding.tvTopic.text = "Topic: ${item.topic}"
            binding.tvQuestion.text = item.question
            binding.tvStatus.text = "Status: ${formatStatus(item.isCorrect)}"
            binding.tvAnsweredAt.text = "Answered at: ${formatNullableTimestamp(item.answeredAt)}"

            binding.llDetails.visibility = if (isExpanded) View.VISIBLE else View.GONE
            binding.tvExpandHint.text = if (isExpanded) "Tap to hide details" else "Tap to view details"

            binding.tvUserAnswer.text = "Your answer: ${item.userAnswer ?: "Not answered"}"
            binding.tvCorrectAnswer.text = "Correct answer: ${item.correctAnswer ?: "Not available"}"

            bindHint(item)
            bindExplanation(item)

            binding.historyCardContainer.setOnClickListener {
                toggleExpanded(item.id)
            }
        }

        private fun bindHint(item: QuestionHistoryItem) {
            if (item.hintResponse.isNullOrBlank()) {
                binding.tvHint.text = "Hint:\nNot requested"
                binding.tvHintTime.visibility = View.GONE
            } else {
                binding.tvHint.text = "Hint:\n${item.hintResponse}"
                binding.tvHintTime.visibility = View.VISIBLE
                binding.tvHintTime.text =
                    "Hint requested at: ${formatNullableTimestamp(item.hintRequestedAt)}"
            }
        }

        private fun bindExplanation(item: QuestionHistoryItem) {
            if (item.explanationResponse.isNullOrBlank()) {
                binding.tvExplanation.text = "Explanation:\nNot requested"
                binding.tvExplanationTime.visibility = View.GONE
            } else {
                binding.tvExplanation.text = "Explanation:\n${item.explanationResponse}"
                binding.tvExplanationTime.visibility = View.VISIBLE
                binding.tvExplanationTime.text =
                    "Explanation requested at: ${formatNullableTimestamp(item.explanationRequestedAt)}"
            }
        }

        private fun toggleExpanded(itemId: String) {
            if (expandedItemIds.contains(itemId)) {
                expandedItemIds.remove(itemId)
            } else {
                expandedItemIds.add(itemId)
            }

            notifyItemChanged(bindingAdapterPosition)
        }

        private fun formatStatus(isCorrect: Boolean?): String {
            return when (isCorrect) {
                true -> "Correct"
                false -> "Incorrect"
                null -> "Not answered yet"
            }
        }

        private fun formatNullableTimestamp(timestamp: Long?): String {
            if (timestamp == null) return "Not available"

            val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }
    }
}