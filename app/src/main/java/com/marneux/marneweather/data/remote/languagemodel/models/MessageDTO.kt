package com.marneux.marneweather.data.remote.languagemodel.models

import com.google.gson.annotations.SerializedName

data class MessageDTO(

    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)
