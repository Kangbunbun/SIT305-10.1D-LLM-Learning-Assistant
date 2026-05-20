package com.example.llm_enhancedlearningassistantapp.network

import com.example.llm_enhancedlearningassistantapp.model.PaymentIntentRequest
import com.example.llm_enhancedlearningassistantapp.model.PaymentIntentResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentService {

    @POST("payment/create-payment-intent")
    suspend fun createPaymentIntent(
        @Body request: PaymentIntentRequest
    ): PaymentIntentResponse
}