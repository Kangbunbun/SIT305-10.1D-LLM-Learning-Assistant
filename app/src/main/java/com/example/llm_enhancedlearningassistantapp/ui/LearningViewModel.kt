package com.example.llm_enhancedlearningassistantapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.llm_enhancedlearningassistantapp.model.ExplainRequest
import com.example.llm_enhancedlearningassistantapp.model.ExplainResponse
import com.example.llm_enhancedlearningassistantapp.model.HintRequest
import com.example.llm_enhancedlearningassistantapp.model.HintResponse
import com.example.llm_enhancedlearningassistantapp.model.Question
import com.example.llm_enhancedlearningassistantapp.model.Task
import com.example.llm_enhancedlearningassistantapp.model.UIState
import com.example.llm_enhancedlearningassistantapp.repository.LearningRepository
import kotlinx.coroutines.launch

class LearningViewModel : ViewModel() {

    private val repository = LearningRepository()

    private val _recommendedTasks = MutableLiveData<List<Task>>()
    val recommendedTasks: LiveData<List<Task>> = _recommendedTasks

    fun loadTasks(interests: Set<String>) {
        _recommendedTasks.value = repository.getTasksForInterests(interests)
    }

    private val _hintStates = MutableLiveData<Map<Int, UIState<HintResponse>>>(emptyMap())
    val hintStates: LiveData<Map<Int, UIState<HintResponse>>> = _hintStates

    fun getHint(question: Question, topic: String) {
        val currentMap = _hintStates.value ?: emptyMap()
        _hintStates.value = currentMap + (question.id to UIState.Loading)

        viewModelScope.launch {
            val result = repository.getHint(
                HintRequest(
                    topic = topic,
                    question = question.text,
                    options = question.options
                )
            )

            _hintStates.value = (_hintStates.value ?: emptyMap()) + (question.id to result)
        }
    }

    private val _explainStates = MutableLiveData<Map<Int, UIState<ExplainResponse>>>(emptyMap())
    val explainStates: LiveData<Map<Int, UIState<ExplainResponse>>> = _explainStates

    fun getExplanation(question: Question, userAnswer: String, topic: String) {
        val currentMap = _explainStates.value ?: emptyMap()
        _explainStates.value = currentMap + (question.id to UIState.Loading)

        viewModelScope.launch {
            val result = repository.getExplanation(
                ExplainRequest(
                    topic = topic,
                    question = question.text,
                    correctAnswer = question.correctAnswer,
                    userAnswer = userAnswer
                )
            )

            _explainStates.value = (_explainStates.value ?: emptyMap()) + (question.id to result)
        }
    }

    fun clearAIStates() {
        _hintStates.value = emptyMap()
        _explainStates.value = emptyMap()
    }
}