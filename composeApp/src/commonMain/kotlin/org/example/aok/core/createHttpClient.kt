package org.example.aok.core

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(): HttpClient {
    return HttpClient() {
        // Plugin para la negociación de contenido
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        // Configuración de tiempo de espera
        install(HttpTimeout) {
            requestTimeoutMillis = 100_000 // Tiempo total de la solicitud
            connectTimeoutMillis = 100_000 // Tiempo para establecer la conexión
            socketTimeoutMillis = 100_000  // Tiempo para esperar datos después de conectar
        }

        // Configuraciones adicionales del motor Android
        engine {
            // Aquí no necesitas definir connectTimeout ni socketTimeout directamente
            // Puedes agregar configuraciones específicas del motor si es necesario
        }
    }
}
