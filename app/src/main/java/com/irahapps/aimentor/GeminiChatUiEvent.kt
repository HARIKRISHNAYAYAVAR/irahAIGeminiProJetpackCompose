package com.irahapps.aimentor
import android.graphics.Bitmap

sealed class GeminiChatUiEvent {
    data class UpdatePrompt(
        val newPrompt: String
    ) : GeminiChatUiEvent()
    data class SendPrompt(
        val prompt: String,
        val bitmap: Bitmap?
    ) : GeminiChatUiEvent()
}
