package com.marneux.marneweather.data.generatedsummary.remote.models

import com.google.gson.annotations.SerializedName

data class TextGenerationPromptBody(
    @SerializedName("messages") val messages: List<MessageDTO>,
    @SerializedName("model") val model: String,
    @SerializedName("max_tokens") val maxResponseTokens: Int = 150
)
