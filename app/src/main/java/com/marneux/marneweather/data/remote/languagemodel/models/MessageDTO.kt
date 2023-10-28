package com.marneux.marneweather.data.remote.languagemodel.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageDTO(
    val role: String,
    val content: String
)