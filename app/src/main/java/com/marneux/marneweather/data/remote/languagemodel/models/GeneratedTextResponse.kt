package com.marneux.marneweather.data.remote.languagemodel.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeneratedTextResponse(
    val id: String,
    val created: Int,
    @Json(name = "choices") val generatedResponses: List<GeneratedResponse>
) {

    @JsonClass(generateAdapter = true)
    data class GeneratedResponse(val message: MessageDTO)
}