package com.marneux.marneweather.data.remote.languagemodel.models

import com.google.gson.annotations.SerializedName

data class TextGenerationPromptBody(
    @SerializedName("messages")
    val messages: List<MessageDTO>,

    @SerializedName("model")
    val model: String,

    @SerializedName("max_tokens")
    val maxResponseTokens: Int = 150
)
