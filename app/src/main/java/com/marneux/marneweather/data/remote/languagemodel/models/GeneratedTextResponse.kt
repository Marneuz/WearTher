package com.marneux.marneweather.data.remote.languagemodel.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

import com.google.gson.annotations.SerializedName

data class GeneratedTextResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("created")
    val created: Int,

    @SerializedName("choices")
    val generatedResponses: List<GeneratedResponse>
) {

    data class GeneratedResponse(
        @SerializedName("message")
        val message: MessageDTO
    )
}
