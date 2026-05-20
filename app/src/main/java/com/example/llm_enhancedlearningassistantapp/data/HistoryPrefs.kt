package com.example.llm_enhancedlearningassistantapp.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

data class QuestionHistoryItem(
    val id: String,
    val topic: String,
    val question: String,
    val userAnswer: String? = null,
    val correctAnswer: String? = null,
    val isCorrect: Boolean? = null,
    val answeredAt: Long? = null,
    val hintResponse: String? = null,
    val hintPrompt: String? = null,
    val hintRequestedAt: Long? = null,
    val explanationResponse: String? = null,
    val explanationPrompt: String? = null,
    val explanationRequestedAt: Long? = null
)

class HistoryPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("history_prefs", Context.MODE_PRIVATE)

    private val userPrefs = UserPrefs(context)

    fun saveOrUpdateQuestionResult(
        topic: String,
        question: String,
        userAnswer: String,
        correctAnswer: String,
        isCorrect: Boolean,
        answeredAt: Long = System.currentTimeMillis()
    ) {
        val items = getHistoryItems().toMutableList()
        val id = createQuestionId(topic, question)

        val existingIndex = items.indexOfFirst { it.id == id }

        if (existingIndex >= 0) {
            val existingItem = items[existingIndex]

            items[existingIndex] = existingItem.copy(
                topic = topic,
                question = question,
                userAnswer = userAnswer,
                correctAnswer = correctAnswer,
                isCorrect = isCorrect,
                answeredAt = answeredAt
            )
        } else {
            items.add(
                0,
                QuestionHistoryItem(
                    id = id,
                    topic = topic,
                    question = question,
                    userAnswer = userAnswer,
                    correctAnswer = correctAnswer,
                    isCorrect = isCorrect,
                    answeredAt = answeredAt
                )
            )
        }

        saveHistoryItems(items)
    }

    fun saveOrUpdateHint(
        topic: String,
        question: String,
        prompt: String,
        aiResponse: String,
        hintRequestedAt: Long = System.currentTimeMillis()
    ) {
        val items = getHistoryItems().toMutableList()
        val id = createQuestionId(topic, question)

        val existingIndex = items.indexOfFirst { it.id == id }

        if (existingIndex >= 0) {
            val existingItem = items[existingIndex]

            items[existingIndex] = existingItem.copy(
                hintPrompt = prompt,
                hintResponse = aiResponse,
                hintRequestedAt = hintRequestedAt
            )
        } else {
            items.add(
                0,
                QuestionHistoryItem(
                    id = id,
                    topic = topic,
                    question = question,
                    hintPrompt = prompt,
                    hintResponse = aiResponse,
                    hintRequestedAt = hintRequestedAt
                )
            )
        }

        saveHistoryItems(items)
    }

    fun saveOrUpdateExplanation(
        topic: String,
        question: String,
        prompt: String,
        aiResponse: String,
        userAnswer: String,
        correctAnswer: String,
        isCorrect: Boolean,
        explanationRequestedAt: Long = System.currentTimeMillis()
    ) {
        val items = getHistoryItems().toMutableList()
        val id = createQuestionId(topic, question)

        val existingIndex = items.indexOfFirst { it.id == id }

        if (existingIndex >= 0) {
            val existingItem = items[existingIndex]

            items[existingIndex] = existingItem.copy(
                userAnswer = userAnswer,
                correctAnswer = correctAnswer,
                isCorrect = isCorrect,
                explanationPrompt = prompt,
                explanationResponse = aiResponse,
                explanationRequestedAt = explanationRequestedAt
            )
        } else {
            items.add(
                0,
                QuestionHistoryItem(
                    id = id,
                    topic = topic,
                    question = question,
                    userAnswer = userAnswer,
                    correctAnswer = correctAnswer,
                    isCorrect = isCorrect,
                    explanationPrompt = prompt,
                    explanationResponse = aiResponse,
                    explanationRequestedAt = explanationRequestedAt
                )
            )
        }

        saveHistoryItems(items)
    }

    fun getHistoryItems(): List<QuestionHistoryItem> {
        val jsonString = prefs.getString(currentHistoryKey(), "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)
        val items = mutableListOf<QuestionHistoryItem>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            val item = QuestionHistoryItem(
                id = jsonObject.optString("id"),
                topic = jsonObject.optString("topic"),
                question = jsonObject.optString("question"),
                userAnswer = jsonObject.optNullableString("userAnswer"),
                correctAnswer = jsonObject.optNullableString("correctAnswer"),
                isCorrect = jsonObject.optNullableBoolean("isCorrect"),
                answeredAt = jsonObject.optNullableLong("answeredAt"),
                hintResponse = jsonObject.optNullableString("hintResponse"),
                hintPrompt = jsonObject.optNullableString("hintPrompt"),
                hintRequestedAt = jsonObject.optNullableLong("hintRequestedAt"),
                explanationResponse = jsonObject.optNullableString("explanationResponse"),
                explanationPrompt = jsonObject.optNullableString("explanationPrompt"),
                explanationRequestedAt = jsonObject.optNullableLong("explanationRequestedAt")
            )

            items.add(item)
        }

        return items
    }

    fun clearHistory() {
        prefs.edit()
            .remove(currentHistoryKey())
            .apply()
    }

    private fun saveHistoryItems(items: List<QuestionHistoryItem>) {
        val jsonArray = JSONArray()

        items.forEach { item ->
            val jsonObject = JSONObject().apply {
                put("id", item.id)
                put("topic", item.topic)
                put("question", item.question)

                putNullable("userAnswer", item.userAnswer)
                putNullable("correctAnswer", item.correctAnswer)
                putNullable("isCorrect", item.isCorrect)
                putNullable("answeredAt", item.answeredAt)

                putNullable("hintResponse", item.hintResponse)
                putNullable("hintPrompt", item.hintPrompt)
                putNullable("hintRequestedAt", item.hintRequestedAt)

                putNullable("explanationResponse", item.explanationResponse)
                putNullable("explanationPrompt", item.explanationPrompt)
                putNullable("explanationRequestedAt", item.explanationRequestedAt)
            }

            jsonArray.put(jsonObject)
        }

        prefs.edit()
            .putString(currentHistoryKey(), jsonArray.toString())
            .apply()
    }

    private fun createQuestionId(topic: String, question: String): String {
        return "${topic.trim().lowercase()}_${question.trim().lowercase()}"
            .replace(" ", "_")
            .replace(Regex("[^a-zA-Z0-9_]"), "")
    }

    private fun currentHistoryKey(): String {
        val email = userPrefs.getEmail()
        return "${KEY_HISTORY_ITEMS}_${sanitizeEmail(email)}"
    }

    private fun sanitizeEmail(email: String): String {
        return email
            .lowercase()
            .replace("@", "_at_")
            .replace(".", "_")
            .replace("-", "_")
    }

    private fun JSONObject.putNullable(key: String, value: Any?) {
        if (value != null) {
            put(key, value)
        }
    }

    private fun JSONObject.optNullableString(key: String): String? {
        return if (has(key) && !isNull(key)) optString(key) else null
    }

    private fun JSONObject.optNullableLong(key: String): Long? {
        return if (has(key) && !isNull(key)) optLong(key) else null
    }

    private fun JSONObject.optNullableBoolean(key: String): Boolean? {
        return if (has(key) && !isNull(key)) optBoolean(key) else null
    }

    companion object {
        private const val KEY_HISTORY_ITEMS = "question_history_items"
    }
}