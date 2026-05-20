package com.example.llm_enhancedlearningassistantapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.llm_enhancedlearningassistantapp.R
import com.example.llm_enhancedlearningassistantapp.data.UserPrefs
import com.example.llm_enhancedlearningassistantapp.databinding.FragmentProfileBinding
import com.example.llm_enhancedlearningassistantapp.ui.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private lateinit var userPrefs: UserPrefs
    private lateinit var profileShareText: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefs = UserPrefs(requireContext())

        loadProfile()
        setupButtons()
    }

    private fun loadProfile() {
        val username = userPrefs.getUsername()
        val email = userPrefs.getEmail()
        val currentPlan = userPrefs.getCurrentPlan()
        val interests = userPrefs.getInterests().joinToString(", ").ifBlank { "None" }
        val totalQuestions = userPrefs.getTotalQuestions()
        val correctAnswers = userPrefs.getCorrectAnswers()
        val incorrectAnswers = userPrefs.getIncorrectAnswers()

        binding.tvUsername.text = "Username: $username"
        binding.tvEmail.text = "Email: $email"
        binding.tvCurrentPlan.text = "Current Plan: $currentPlan"
        binding.tvInterests.text = "Interests: $interests"
        binding.tvTotalQuestions.text = "Total Questions: $totalQuestions"
        binding.tvCorrectAnswers.text = "Correct Answers: $correctAnswers"
        binding.tvIncorrectAnswers.text = "Incorrect Answers: $incorrectAnswers"

        profileShareText = """
            Learning Assistant Profile
            
            Username: $username
            Email: $email
            Current Plan: $currentPlan
            Interests: $interests
            
            Total Questions: $totalQuestions
            Correct Answers: $correctAnswers
            Incorrect Answers: $incorrectAnswers
            
            Shared from LLM-Enhanced Learning Assistant App
        """.trimIndent()
    }

    private fun setupButtons() {
        binding.btnShareProfile.setOnClickListener {
            shareProfile()
        }

        binding.btnBackHome.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnGoHistory.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_historyFragment)
        }

        binding.btnGoUpgrade.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_upgradeFragment)
        }
    }

    private fun shareProfile() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Learning Assistant Profile")
            putExtra(Intent.EXTRA_TEXT, profileShareText)
        }

        startActivity(Intent.createChooser(shareIntent, "Share Profile"))
    }
}