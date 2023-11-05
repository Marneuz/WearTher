package com.marneux.marneweather.data.generatedsummary

import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextDatabaseDao
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextForLocationEntity
import com.marneux.marneweather.data.generatedsummary.remote.TextGeneratorClient
import com.marneux.marneweather.data.generatedsummary.remote.models.MessageDTO
import com.marneux.marneweather.data.generatedsummary.remote.models.TextGenerationPromptBody
import com.marneux.marneweather.data.getBodyOrThrowException
import com.marneux.marneweather.domain.cajondesastre.location.models.weather.CurrentWeather
import com.marneux.marneweather.domain.repositories.textgenerator.GenerativeTextRepository
import kotlinx.coroutines.CancellationException


class GenerativeTextRepositoryImpl(
    private val textGeneratorClient: TextGeneratorClient,
    private val generatedTextDatabaseDao: GeneratedTextDatabaseDao,
) : GenerativeTextRepository {

    override suspend fun generateTextForWeatherDetails(weatherDetails: CurrentWeather): Result<String> {
        val generatedTextEntity =
            generatedTextDatabaseDao.getSavedGeneratedTextForDetails(
                nameLocation = weatherDetails.nameLocation,
                temperature = weatherDetails.temperatureRoundedToInt,
                conciseWeatherDescription = weatherDetails.weatherCondition
            )
        if (generatedTextEntity != null) return Result.success(generatedTextEntity.generatedDescription)
        // idioma hardcodeado por el moemento
        // problemas al realizar peticion en español
        //TODO asignar variable de idioma de sistema
        val systemPrompt = """
            Think that you are a dressmaker and you have to say what clothes to wear, it has to be brief,
              a brief description of the time with the parameters that I give you below, and
              then you have to tell me top, bottom, accessories, and footwear .
        """.trimIndent()
        // prompts
        val userPrompt = """
            location = ${weatherDetails.nameLocation};
            currentTemperature = ${weatherDetails.temperatureRoundedToInt};
            weatherCondition = ${weatherDetails.weatherCondition};
            isNight = ${weatherDetails.isDay != 1}
        """.trimIndent()

        val promptMessages = listOf(
            MessageDTO(role = "system", content = systemPrompt),
            MessageDTO(role = "user", content = userPrompt)
        )
        val textGenerationPrompt = TextGenerationPromptBody(
            messages = promptMessages,
            model = "gpt-3.5-turbo-0613"
        )
        // peticion de generacion de texto
        return try {
            // genera texto
            val generatedTextResponse = textGeneratorClient.getModelResponseForConversations(
                textGenerationPostBody = textGenerationPrompt
            ).getBodyOrThrowException()
                .generatedResponses
                .first().message
                .content
            // guarda el texto generado para agilizar futuras peticiones
            val generatedTextForLocationEntity = GeneratedTextForLocationEntity(
                nameLocation = weatherDetails.nameLocation,
                temperature = weatherDetails.temperatureRoundedToInt,
                conciseWeatherDescription = weatherDetails.weatherCondition,
                generatedDescription = generatedTextResponse
            )
            generatedTextDatabaseDao.addGeneratedTextForLocation(generatedTextForLocationEntity)
            Result.success(generatedTextResponse)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            Result.failure(exception)
        }
    }

}