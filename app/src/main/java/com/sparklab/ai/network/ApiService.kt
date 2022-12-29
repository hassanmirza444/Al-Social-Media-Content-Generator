package com.sparklab.ai.network


import android.util.Log
import com.google.gson.JsonObject
import com.sparklab.ai.OpenAIResponse
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class ApiService @Inject constructor() {


    val client = HttpClient(Android) {
        install(DefaultRequest) {
            headers.append("Content-Type", "application/json")
            headers.append(
                "Authorization",
                "Bearer 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"
            )
        }



        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        install(Logging) {
           logger =   object : Logger{
               override fun log(message: String) {
                   Log.d("hssn",message)
               }

           }

        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }

        engine {
            connectTimeout = 100_000
            socketTimeout = 100_000
        }
    }

    suspend fun getData(json: JsonObject): OpenAIResponse {
        return client.post {
            url("https://api.openai.com/v1/completions")
            body= json
        }
    }


}