package com.example.llm_enhancedlearningassistantapp.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.llm_enhancedlearningassistantapp.R
import com.example.llm_enhancedlearningassistantapp.data.UserPrefs
import com.example.llm_enhancedlearningassistantapp.databinding.FragmentSignUpBinding
import com.example.llm_enhancedlearningassistantapp.ui.BaseFragment

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private lateinit var userPrefs: UserPrefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefs = UserPrefs(requireContext())

        binding.btnCreateAccount.setOnClickListener {
            if (validateInput()) {
                val username = binding.etUsername.text.toString().trim()
                val email = binding.etEmail.text.toString().trim()

                userPrefs.saveUsername(username)
                userPrefs.saveEmail(email)
                userPrefs.saveCurrentPlan("Starter")

                findNavController().navigate(R.id.action_signUpFragment_to_interestsFragment)
            }
        }
    }

    private fun validateInput(): Boolean {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val confirmEmail = binding.etConfirmEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email != confirmEmail) {
            Toast.makeText(requireContext(), "Emails do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}