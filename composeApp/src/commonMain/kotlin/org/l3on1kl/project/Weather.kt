package org.l3on1kl.project

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class WeatherService(private val apiKey: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getWeather(city: String): WeatherResponse? {
        try {
            val currentTimeMillis = Clock.System.now().toEpochMilliseconds()
            println("Current time in milliseconds: $currentTimeMillis")

            val response: HttpResponse = client.get("https://api.weatherapi.com/v1/current.json") {
                parameter("key", apiKey)
                parameter("q", city)
                parameter("aqi", "no")
                parameter("lang", "ru")
            }
            return response.body()
        } catch (e: Exception) {
            println("Error fetching weather data: ${e.message}")
            return null
        }
    }
}

@Serializable
data class WeatherResponse(
    val location: Location,
    val current: Current
)

@Serializable
data class Location(
    val name: String,
    val region: String,
    val country: String
)

@Serializable
data class Current(
    val temp_c: Float,
    val condition: Condition
)

@Serializable
data class Condition(
    val text: String,
    val icon: String
)
