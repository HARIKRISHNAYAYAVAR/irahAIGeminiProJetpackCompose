package com.irahapps.aimentor

import android.graphics.Bitmap
import com.irahapps.aimentor.data.GeminiChat

data class GeminiChatState (
    val geminiChatList: MutableList<GeminiChat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)