package com.sparklab.ai.repository


import com.google.gson.JsonObject
import com.sparklab.ai.OpenAIResponse
import com.sparklab.ai.network.ApiService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import javax.inject.Inject

class MainRepository
@Inject
constructor(private val apiService: ApiService) {


    fun getResponseData(obj : JsonObject) : Flow<OpenAIResponse> = flow{
         emit(apiService.getData(obj))
    }.flowOn(IO)
}