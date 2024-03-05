package com.irahapps.aimentor.data

import android.graphics.Bitmap

data class GeminiChat (
    val prompt: String,
    val bitmap: Bitmap?,
    val isFromUser: Boolean
)