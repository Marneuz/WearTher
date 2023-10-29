package com.marneux.marneweather.data.remote.languagemodel.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextGenerationPromptBody(
    val messages: List<MessageDTO>,
    val model: String,
    @Json(name = "max_tokens") val maxResponseTokens: Int = 150
)