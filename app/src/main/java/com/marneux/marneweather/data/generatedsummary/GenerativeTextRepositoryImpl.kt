package com.marneux.marneweather.data.generatedsummary

import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextDao
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextEntity
import com.marneux.marneweather.data.generatedsummary.mapper.weatherCodeStringMap
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

    // Definición de constantes y plantillas utilizadas para generar texto.
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

    // Función principal para generar texto basado en los detalles del clima.
    override suspend fun generateTextWeatherDetails(weatherDetails: CurrentWeather):
            Result<String> {
        val systemLanguageCode = Locale.getDefault().language
        return generatedTextDao.getSavedGeneratedTextForDetails(
            nameLocation = weatherDetails.nameLocation,
            temperature = weatherDetails.temperatureRoundedToInt,
            conciseWeatherDescription = weatherCodeStringMap.getValue(weatherDetails.shortDescriptionCode)
        )?.let { Result.success(it.generatedDescription) }
            ?: generateAndSaveText(weatherDetails, systemLanguageCode)
    }

    // Función para generar y guardar el texto.
    private suspend fun generateAndSaveText(
        weatherDetails: CurrentWeather,
        systemLanguageCode: String
    ): Result<String> {
        // Genera el prompt del usuario y prepara el cuerpo de la petición.
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

            // Realiza la petición al cliente de generación de texto y procesa la respuesta.
            val generatedTextResponse =
                textGeneratorClient.getAIModelResponse(textGenerationPrompt)
                    .getBodyOrThrowException()
                    .generatedResponses
                    .first().message
                    .content
            saveGeneratedText(weatherDetails, generatedTextResponse)
            Result.success(generatedTextResponse)
        } catch (exception: Exception) {
            // Maneja las excepciones y devuelve un resultado de error.
            exception.handleRepositoryException()
        }
    }

    // Función auxiliar para generar el prompt del usuario.
    private fun generateUserPrompt(
        weatherDetails: CurrentWeather,
        systemLanguageCode: String
    ): String {
        return """
            Location = ${weatherDetails.nameLocation};
            Current temperature = ${weatherDetails.temperatureRoundedToInt};
            Weather Condition = ${weatherDetails.shortDescriptionCode};
            Is Night = ${weatherDetails.isDay != 1};
            Used language for response = $systemLanguageCode
        """.trimIndent()
    }

    // Función para guardar el texto generado en la base de datos.
    private suspend fun saveGeneratedText(weatherDetails: CurrentWeather, generatedText: String) {
        val generatedTextEntity = GeneratedTextEntity(
            nameLocation = weatherDetails.nameLocation,
            temperature = weatherDetails.temperatureRoundedToInt,
            conciseWeatherDescription = weatherCodeStringMap.getValue(weatherDetails.shortDescriptionCode),
            generatedDescription = generatedText
        )
        generatedTextDao.addGeneratedTextForLocation(generatedTextEntity)
    }
}
