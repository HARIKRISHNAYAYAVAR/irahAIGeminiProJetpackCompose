package com.irahapps.aimentor.data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object GeminiChatData {

    val api_key = "AIzaSyBtPQG2TNshIZGkNe3DjTfMRdXq8gF1ju0"

    suspend fun getGeminiResponse(prompt: String): GeminiChat {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro", apiKey = api_key
        )

        try {
            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(prompt)
            }

            return GeminiChat(
                prompt = response.text ?: "error",
                bitmap = null,
                isFromUser = false
            )

        } catch (e: Exception) {
            return GeminiChat(
                prompt = e.message ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }

    }

    suspend fun getGeminiResponseWithImage(prompt: String, bitmap: Bitmap): GeminiChat {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision", apiKey = api_key
        )

        try {

            val inputContent = content {
                image(bitmap)
                text(prompt)
            }

            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(inputContent)
            }

            return GeminiChat(
                prompt = response.text ?: "error",
                bitmap = null,
                isFromUser = false
            )

        } catch (e: Exception) {
            return GeminiChat(
                prompt = e.message ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }

    }

}




















