package com.marneux.marneweather.data.generatedsummary

import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextDatabaseDao
import com.marneux.marneweather.data.generatedsummary.database.GeneratedTextForLocationEntity
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
    private val generatedTextDatabaseDao: GeneratedTextDatabaseDao,
) : GenerativeTextRepository {

    companion object {
        private const val MODEL_ID = "gpt-3.5-turbo-1106"
        private val SYSTEM_PROMPT = """
            Proporciona una recomendación breve y concisa de vestimenta adecuada para un 
            hombre, siguiendo el formato especificado a continuación:
            
            -Parte superior:(chaquetas, camisas, camisetas, 
            abrigos, etc...)
            
            -Parte inferior: (pantalones, faldas, bermudas, etc...)
            
            -Calzado: (zapatillas, sandalias, botas, etc...)
            
            -Accesorios: (gorros, bufandas, relojes, etc...)
            
            Cada sugerencia debe de ser muy breve y estar bien coordinada y alineada con las 
            tendencias actuales de la moda masculina. 
            Tu respuesta deberá ser proporcionada en el idioma que se te indica mediante el 
            código de idioma del sistema. Traduce las cabeceras tambien ( Parte superior, parte 
            inferior, calzado, accesorios )
        """.trimIndent()
    }

    override suspend fun generateTextForWeatherDetails(weatherDetails: CurrentWeather): Result<String> {
        val systemLanguageCode = Locale.getDefault().language
        return generatedTextDatabaseDao.getSavedGeneratedTextForDetails(
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
        val generatedTextForLocationEntity = GeneratedTextForLocationEntity(
            nameLocation = weatherDetails.nameLocation,
            temperature = weatherDetails.temperatureRoundedToInt,
            conciseWeatherDescription = weatherDetails.weatherCondition,
            generatedDescription = generatedText
        )
        generatedTextDatabaseDao.addGeneratedTextForLocation(generatedTextForLocationEntity)
    }
}
