package com.marneux.marneweather.data.generatedsummary.remote.models

import com.google.gson.annotations.SerializedName

data class GeneratedTextResponse(
    @SerializedName("id") val id: String,
    @SerializedName("created") val created: Int,
    @SerializedName("choices") val generatedResponses: List<GeneratedResponse>
) {

    data class GeneratedResponse(
        @SerializedName("message") val message: MessageDTO
    )
}
