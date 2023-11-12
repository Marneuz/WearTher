package com.marneux.marneweather.data.generatedsummary

import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextDao
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextEntity
import com.marneux.marneweather.data.generatedsummary.remote.TextGeneratorClient
import com.marneux.marneweather.data.generatedsummary.remote.models.MessageDTO
import com.marneux.marneweather.data.generatedsummary.remote.models.TextGenerationPromptBody
import com.marneux.marneweather.data.getBodyOrThrowException
import com.marneux.marneweather.data.handleRepositoryException
import com.marneux.marneweather.domain.repositories.textgenerator.GenerativeTextRepository
import com.marneux.marneweather.model.weather.CurrentWeather
import java.util.Locale

class GenerativeTextRepositoryImpl(
    private val textGeneratorClient: TextGeneratorClient,
    private val generatedTextDao: GeneratedTextDao,
) : GenerativeTextRepository {


    companion object {

        private const val MODEL_ID = "gpt-3.5-turbo-1106"
        private val SYSTEM_PROMPT = """
            "Provide a brief and concise weather description and then procced to make a clothing recommendation for a man, following the format specified below:

            -Top: (jackets, shirts, t-shirts, coats, etc.)

            -Bottom: (pants, skirts, shorts, etc.)

            -Footwear: (sneakers, sandals, boots, etc.)

            -Accessories: (hats, scarves, watches, etc.)

            Each suggestion should be very brief and well-coordinated in style and colors, 
            aligning with current trends in men's fashion. Your response should be provided in 
            the language indicated by the system's language code. 
            Translate the headings as well (Top, Bottom, Footwear, Accessories)."
      """.trimIndent()
    }

    override suspend fun generateTextForWeatherDetails(weatherDetails: CurrentWeather): Result<String> {
        val systemLanguageCode = Locale.getDefault().language
        return generatedTextDao.getSavedGeneratedTextForDetails(
            nameLocation = weatherDetails.nameLocation,
            temperature = weatherDetails.temperatureRoundedToInt,
            conciseWeatherDescription = weatherDetails.weatherCondition
        )?.let { Result.success(it.generatedDescription) }
            ?: generateAndSaveText(weatherDetails, systemLanguageCode)
    }

    private suspend fun generateAndSaveText(
        weatherDetails: CurrentWeather,
        systemLanguageCode: String
    ): Result<String> {
        val userPrompt = generateUserPrompt(weatherDetails, systemLanguageCode)
        val textGenerationPrompt = TextGenerationPromptBody(
            messages = listOf(
                MessageDTO(role = "system", content = SYSTEM_PROMPT),
                MessageDTO(role = "user", content = userPrompt)
            ),
            model = MODEL_ID,
            maxResponseTokens = 150
        )
        return try {
            val generatedTextResponse =
                textGeneratorClient.getModelResponseForConversations(textGenerationPrompt)
                    .getBodyOrThrowException()
                    .generatedResponses
                    .first().message
                    .content
            saveGeneratedText(weatherDetails, generatedTextResponse)
            Result.success(generatedTextResponse)
        } catch (exception: Exception) {
            exception.handleRepositoryException()
        }
    }

    private fun generateUserPrompt(
        weatherDetails: CurrentWeather,
        systemLanguageCode: String
    ): String {
        return """
            Location = ${weatherDetails.nameLocation};
            Current temperature = ${weatherDetails.temperatureRoundedToInt};
            Weather Condition = ${weatherDetails.weatherCondition};
            Is Night = ${weatherDetails.isDay != 1};
            Used language for response = $systemLanguageCode
        """.trimIndent()
    }

    private suspend fun saveGeneratedText(weatherDetails: CurrentWeather, generatedText: String) {
        val generatedTextEntity = GeneratedTextEntity(
            nameLocation = weatherDetails.nameLocation,
            temperature = weatherDetails.temperatureRoundedToInt,
            conciseWeatherDescription = weatherDetails.weatherCondition,
            generatedDescription = generatedText
        )
        generatedTextDao.addGeneratedTextForLocation(generatedTextEntity)
    }
}
