package com.marneux.marneweather.data.remote.languagemodel

import com.marneux.marneweather.data.remote.languagemodel.models.GeneratedTextResponse
import com.marneux.marneweather.data.remote.languagemodel.models.TextGenerationPromptBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TextGeneratorClient {

    /**
     * Returns a response containing the generated text based on the provided prompt.
     * @param textGenerationPostBody The request body containing the prompt and LLM model information.
     * @return A response containing the generated text.
     */
    @POST(TextGeneratorClientConstants.Endpoints.CHAT_COMPLETION_END_POINT)
    suspend fun getModelResponseForConversations(
        @Body textGenerationPostBody: TextGenerationPromptBody
    ): Response<GeneratedTextResponse>

}
