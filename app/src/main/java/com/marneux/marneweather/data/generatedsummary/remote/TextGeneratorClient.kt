package com.marneux.marneweather.data.generatedsummary.remote

import com.marneux.marneweather.data.generatedsummary.remote.models.GeneratedTextResponse
import com.marneux.marneweather.data.generatedsummary.remote.models.TextGenerationPromptBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TextGeneratorClient {


    @POST(TextGeneratorClientConstants.Endpoints.CHAT_COMPLETION_END_POINT)
    suspend fun getModelResponseForConversations(
        @Body textGenerationPostBody: TextGenerationPromptBody
    ): Response<GeneratedTextResponse>

}
