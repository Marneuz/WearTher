package com.marneux.marneweather.data.generatedsummary.remote.models

import com.google.gson.annotations.SerializedName

data class MessageDTO(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)
