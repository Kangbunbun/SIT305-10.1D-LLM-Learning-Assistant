package com.example.llm_enhancedlearningassistantapp.data

import android.content.Context
import android.content.SharedPreferences

class UserPrefs(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUsername(username: String) {
        prefs.edit().putString(userKey(KEY_USERNAME), username).apply()
    }

    fun getUsername(): String =
        prefs.getString(userKey(KEY_USERNAME), "Student") ?: "Student"

    fun saveEmail(email: String) {
        prefs.edit()
            .putString(KEY_CURRENT_EMAIL, email)
            .putString(userKey(KEY_EMAIL, email), email)
            .apply()
    }

    fun getEmail(): String =
        prefs.getString(KEY_CURRENT_EMAIL, "student@example.com") ?: "student@example.com"

    fun saveInterests(interests: Set<String>) {
        prefs.edit().putStringSet(userKey(KEY_INTERESTS), interests).apply()
    }

    fun getInterests(): Set<String> =
        prefs.getStringSet(userKey(KEY_INTERESTS), emptySet()) ?: emptySet()

    fun saveCurrentPlan(plan: String) {
        prefs.edit().putString(userKey(KEY_CURRENT_PLAN), plan).apply()
    }

    fun getCurrentPlan(): String =
        prefs.getString(userKey(KEY_CURRENT_PLAN), "Starter") ?: "Starter"

    fun addQuizResult(totalQuestions: Int, correctAnswers: Int) {
        val currentTotal = getTotalQuestions()
        val currentCorrect = getCorrectAnswers()
        val currentIncorrect = getIncorrectAnswers()

        val incorrectAnswers = totalQuestions - correctAnswers

        prefs.edit()
            .putInt(userKey(KEY_TOTAL_QUESTIONS), currentTotal + totalQuestions)
            .putInt(userKey(KEY_CORRECT_ANSWERS), currentCorrect + correctAnswers)
            .putInt(userKey(KEY_INCORRECT_ANSWERS), currentIncorrect + incorrectAnswers)
            .apply()
    }

    fun getTotalQuestions(): Int =
        prefs.getInt(userKey(KEY_TOTAL_QUESTIONS), 0)

    fun getCorrectAnswers(): Int =
        prefs.getInt(userKey(KEY_CORRECT_ANSWERS), 0)

    fun getIncorrectAnswers(): Int =
        prefs.getInt(userKey(KEY_INCORRECT_ANSWERS), 0)

    fun clearCurrentUserData() {
        val currentEmail = getEmail()

        prefs.edit()
            .remove(userKey(KEY_USERNAME, currentEmail))
            .remove(userKey(KEY_EMAIL, currentEmail))
            .remove(userKey(KEY_INTERESTS, currentEmail))
            .remove(userKey(KEY_CURRENT_PLAN, currentEmail))
            .remove(userKey(KEY_TOTAL_QUESTIONS, currentEmail))
            .remove(userKey(KEY_CORRECT_ANSWERS, currentEmail))
            .remove(userKey(KEY_INCORRECT_ANSWERS, currentEmail))
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    private fun userKey(key: String): String {
        return userKey(key, getEmail())
    }

    private fun userKey(key: String, email: String): String {
        return "${key}_${sanitizeEmail(email)}"
    }

    private fun sanitizeEmail(email: String): String {
        return email
            .lowercase()
            .replace("@", "_at_")
            .replace(".", "_")
            .replace("-", "_")
    }

    companion object {
        private const val KEY_CURRENT_EMAIL = "current_email"

        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_INTERESTS = "interests"
        private const val KEY_CURRENT_PLAN = "current_plan"
        private const val KEY_TOTAL_QUESTIONS = "total_questions"
        private const val KEY_CORRECT_ANSWERS = "correct_answers"
        private const val KEY_INCORRECT_ANSWERS = "incorrect_answers"
    }
}