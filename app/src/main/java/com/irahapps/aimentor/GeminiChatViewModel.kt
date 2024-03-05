package com.irahapps.aimentor

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irahapps.aimentor.data.GeminiChat
import com.irahapps.aimentor.data.GeminiChatData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class GeminiChatViewModel : ViewModel() {

    private val _Gemini_chatState = MutableStateFlow(GeminiChatState())
    val chatState = _Gemini_chatState.asStateFlow()

    fun onEvent(event: GeminiChatUiEvent) {
        when (event) {
            is GeminiChatUiEvent.SendPrompt -> {
                if (event.prompt.isNotEmpty()) {

                    addPrompt(event.prompt, event.bitmap)

                    if (event.bitmap != null) {
                        getResponseWithImage(event.prompt, event.bitmap)
                    } else {
                        getResponse(event.prompt)
                    }
                }
            }

            is GeminiChatUiEvent.UpdatePrompt -> {
                _Gemini_chatState.update {
                    it.copy(prompt = event.newPrompt)
                }
            }

        }
    }

    private fun addPrompt(prompt: String, bitmap: Bitmap?) {
        _Gemini_chatState.update {
            it.copy(
                geminiChatList = it.geminiChatList.toMutableList().apply {
                    add(0, GeminiChat(prompt, bitmap, true))
                },
                prompt = "",
                bitmap = null
            )
        }
    }

    private fun getResponse(prompt: String) {
        viewModelScope.launch {
            val chat = GeminiChatData.getGeminiResponse(prompt)
            _Gemini_chatState.update {
                it.copy(
                    geminiChatList = it.geminiChatList.toMutableList().apply {
                        add(0, chat)
                    }
                )
            }
        }
    }

    private fun getResponseWithImage(prompt: String, bitmap: Bitmap) {
        viewModelScope.launch {
            val chat = GeminiChatData.getGeminiResponseWithImage(prompt, bitmap)
            _Gemini_chatState.update {
                it.copy(
                    geminiChatList = it.geminiChatList.toMutableList().apply {
                        add(0, chat)
                    }
                )
            }
        }
    }
}


















